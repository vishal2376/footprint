# Footprint - Where you go, the world reveals

I'm Vishal Singh from Himachal Pradesh, India. Just graduated B.Tech from NIT Hamirpur in 2025,
though I've been building Android apps for about five years now—mostly self-taught through docs,
codelabs, and a lot of trial and error. I've shipped a few apps that people actually use (Git Coach,
Snaptick), and I spend way too much time tinkering with custom animations, Canvas drawings, and
lately, graphics shaders. For the past two years, Kotlin Multiplatform has been my main obsession.
I've built everything from book apps to a Rock Paper Scissors game running on Android, iOS, Desktop,
and Web.

So, Footprint. The idea came from my evening walks in Bangalore. I'd wander through random lanes,
find cool spots, and then realize that I have no clue how much of this city I've actually explored.
Then I thought about games like Hollow Knight or fog of war style strategy games. Why not do that
for real world exploration?

That's the core concept: unexplored map areas show mysterious dark tiles with "?" marks. Walk through them, and they reveal permanently. It tracks your total explored tiles, distance walked, and even what tiny fraction of the world you've uncovered. Kinda turns walking into a game.

Techwise, it's Kotlin Multiplatform with about 80% shared code between Android and iOS. Compose
Multiplatform handles UI, MapCompose renders OSM tiles, Room stores exploration data locally. The
tricky part was generating those mystery tiles differently on each platform—Android uses Canvas, iOS
uses CoreGraphics—so I used expect/actual declarations to keep platform code separate but logic
shared.

If you want to see more of my work, feel free to check out my
Twitter [@vishal2376](https://x.com/vishal2376)