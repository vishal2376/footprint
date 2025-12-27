import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.kotlinSerialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.androidx.room)
}

kotlin {
	androidTarget {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}
	}

	listOf(
		iosArm64(),
		iosSimulatorArm64()
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "ComposeApp"
			isStatic = true
		}
	}

	room {
		schemaDirectory("$projectDir/schemas")
	}

	sourceSets {
		androidMain.dependencies {
			implementation(compose.preview)
			implementation(libs.androidx.activity.compose)

			// ktor
			implementation(libs.ktor.client.android)

			// koin
			implementation(libs.koin.android)
		}
		commonMain.dependencies {
			implementation(compose.runtime)
			implementation(compose.foundation)
			implementation(compose.material3)
			implementation(compose.ui)
			implementation(compose.components.resources)
			implementation(compose.components.uiToolingPreview)
			implementation(libs.androidx.lifecycle.viewmodelCompose)
			implementation(libs.androidx.lifecycle.runtimeCompose)

			// ktor
			implementation(libs.ktor.client.core)

			// mapcompose for tile maps
			implementation(libs.mapcompose.mp)

			// koin
			implementation(libs.koin.core)
			implementation(libs.koin.compose)
			implementation(libs.koin.compose.viewmodel)

			// navigation
			implementation(libs.navigation.compose)

			// material icons
			implementation(compose.materialIconsExtended)

			// Room DB
			implementation(libs.androidx.room.runtime)
			implementation(libs.androidx.sqlite.bundled)

			// DateTime
			implementation(libs.kotlinx.datetime)
		}

		iosMain.dependencies {
			// ktor
			implementation(libs.ktor.client.darwin)
		}

		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
	}
}

android {
	namespace = "com.nullappstudios.footprint"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "com.nullappstudios.footprint"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = 1
		versionName = "1.0"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
}

dependencies {
	debugImplementation(compose.uiTooling)
	implementation(libs.play.services.location)

	// Room
	add("kspAndroid", libs.androidx.room.compiler)
	add("kspIosSimulatorArm64", libs.androidx.room.compiler)
	add("kspIosArm64", libs.androidx.room.compiler)
}
