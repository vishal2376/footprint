package com.nullappstudios.footprint.data.datasource

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGContextAddLineToPoint
import platform.CoreGraphics.CGContextFillRect
import platform.CoreGraphics.CGContextMoveToPoint
import platform.CoreGraphics.CGContextSetLineWidth
import platform.CoreGraphics.CGContextSetRGBFillColor
import platform.CoreGraphics.CGContextSetRGBStrokeColor
import platform.CoreGraphics.CGContextStrokePath
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSString
import platform.UIKit.NSFontAttributeName
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImagePNGRepresentation
import platform.UIKit.drawAtPoint
import platform.UIKit.sizeWithAttributes

@OptIn(ExperimentalForeignApi::class)
actual fun generateMysteryTile(
	col: Int,
	row: Int,
	zoomLevel: Int,
	gridSize: Int,
): ByteArray {
	val tileSize = 256.0
	val size = CGSizeMake(tileSize, tileSize)

	UIGraphicsBeginImageContextWithOptions(size, false, 1.0)
	val context = UIGraphicsGetCurrentContext()
		?: throw IllegalStateException("Failed to get graphics context")

	// Dark background
	CGContextSetRGBFillColor(context, 0.0, 0.016, 0.031, 1.0) // #000408
	CGContextFillRect(context, CGRectMake(0.0, 0.0, tileSize, tileSize))

	// Calculate position in grid
	val gridX = col % gridSize
	val gridY = row % gridSize

	// Border color #181823 = RGB(24, 24, 35)
	CGContextSetRGBStrokeColor(context, 0.094, 0.094, 0.137, 1.0)
	CGContextSetLineWidth(context, 5.0)

	// Draw borders
	if (gridY == 0) {
		// Top border
		CGContextMoveToPoint(context, 0.0, 1.0)
		CGContextAddLineToPoint(context, tileSize, 1.0)
		CGContextStrokePath(context)
	}
	if (gridY == gridSize - 1) {
		// Bottom border
		CGContextMoveToPoint(context, 0.0, tileSize - 1.0)
		CGContextAddLineToPoint(context, tileSize, tileSize - 1.0)
		CGContextStrokePath(context)
	}
	if (gridX == 0) {
		// Left border
		CGContextMoveToPoint(context, 1.0, 0.0)
		CGContextAddLineToPoint(context, 1.0, tileSize)
		CGContextStrokePath(context)
	}
	if (gridX == gridSize - 1) {
		// Right border
		CGContextMoveToPoint(context, tileSize - 1.0, 0.0)
		CGContextAddLineToPoint(context, tileSize - 1.0, tileSize)
		CGContextStrokePath(context)
	}

	// Draw question mark only in center tiles
	val centerStart = gridSize / 2 - if (gridSize % 2 == 0) 1 else 0
	val centerEnd = gridSize / 2

	if (gridX in centerStart..centerEnd && gridY in centerStart..centerEnd) {
		// Scale question mark size based on zoom level
		val zoomScale = when {
			zoomLevel >= 12 -> 0.6f
			zoomLevel >= 9 -> 1.0f
			else -> 1.3f
		}

		val fontSize = 80.0 * gridSize * zoomScale
		val gridCenter = (gridSize - 1) / 2.0
		val centerOffsetX = tileSize / 2.0 + (gridCenter - gridX) * tileSize
		val centerOffsetY = tileSize / 2.0 + (gridCenter - gridY) * tileSize

		// Text color #999999
		val text = "?" as NSString
		val font = UIFont.systemFontOfSize(fontSize)
		val textColor = UIColor.colorWithRed(0.265, 0.265, 0.239, alpha = 1.0)

		@Suppress("UNCHECKED_CAST")
		val attributes = mapOf<Any?, Any?>(
			NSFontAttributeName to font,
			NSForegroundColorAttributeName to textColor
		)

		val textSize = text.sizeWithAttributes(attributes)
		val textX = centerOffsetX - textSize.useContents { width } / 2.0
		val textY = centerOffsetY - textSize.useContents { height } / 2.0

		text.drawAtPoint(CGPointMake(textX, textY), withAttributes = attributes)
	}

	val image = UIGraphicsGetImageFromCurrentImageContext()
		?: throw IllegalStateException("Failed to get image from context")
	UIGraphicsEndImageContext()

	// Convert to PNG data
	val pngData = UIImagePNGRepresentation(image)
		?: throw IllegalStateException("Failed to create PNG data")

	return pngData.bytes?.readBytes(pngData.length.toInt()) ?: byteArrayOf()
}
