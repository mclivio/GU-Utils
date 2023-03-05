# GU-Utils
A simple utility app for "Gods Unchained".

## What does it do:grey_question:
It lets you use different functionalities separated by sections in the bottom navigation:
+ Card Section
    + Search cards and filter them by God, Mana, Rarity or Tribe.
    + Add/Remove card to your deck.
    + Show card design on full screen when clicked.
    
<img src="https://user-images.githubusercontent.com/97415092/220441649-810e64b6-a680-44b9-bce1-e31d1c25054e.jpg" width="250" height="500"> <img src="https://user-images.githubusercontent.com/97415092/220441670-8a234e5b-f26a-47ae-8be5-c44b4fb9e51c.jpg" width="250" height="500">
+ Profile Section
    + Search a profile and show related data like the amount of matches won/lost, total experience or experience needed to level up.
    
<img src="https://user-images.githubusercontent.com/97415092/220441691-0f43bec7-7a1d-442c-8231-4f7c8827683c.jpg" width="250" height="500">

+ History Section
    + Search a profile and show the player's matches sorted by date. This lets you see who won each of your latest matches, which God each player was using and how many turns the match lasted.
    
<img src="https://user-images.githubusercontent.com/97415092/220441697-6b4b942b-95e3-4aed-a18c-8e1c8ef89f99.jpg" width="250" height="500">  

+ Deck Section
    + Create a new Deck by selecting your desired god.
    + Remove cards from the deck.
    + Copy the deck. This encodes it into a valid GU-String which you can use to import the deck into the game.
    
<img src="https://user-images.githubusercontent.com/97415092/220441731-7a71e62a-6c8e-42dc-8f7b-3ce99bb12b8b.jpg" width="250" height="500"> <img src="https://user-images.githubusercontent.com/97415092/220441739-c9bd40cf-de52-42ef-a296-236ca8fea509.jpg" width="250" height="500"> <img src="https://user-images.githubusercontent.com/97415092/220441749-609143ff-55fd-403d-bb81-94807774d3d5.jpg" width="250" height="500">
## Why was this made :eyes:
There are websites that let you do the same things but they either don't have all of these functionalities or they do but the site takes an eternity to load. Since I hate having to spend a lot of time waiting for the site to load, I thought: Well, why don't I make an app that does the same?

## What will be added in future releases :package:
This was mostly done used the official API but sadly not only is it poorly documented but also most of the endpoints do not work. 
Therefore, I'm quite limited to just a few things:
1. Right now the priority is to make a "decode" function that let's you copy your in-game deck and import it into the App.
2. Once that is done, I will probably make the design responsive as right now scaling it to smaller screens makes some things unreadable.

However, the way it is coded makes it hard to do:

<img src="https://user-images.githubusercontent.com/97415092/220442617-0b6135e4-0d08-44c1-a57f-7454aedb1d05.png" width="250" height="500">

Taking Finnian as an example, his library_id is L8-018. When you put the card into your deck, the game follows this process:
A library_id gets coded into 3 letters:
$$LY-XXX → A B C$$
$$A = Y$$
$$B = XXX / 52$$
$$C = XXX \\% 52$$
Since 26 is the length of the American alphabet, this 52 is the length if we take both uppercase and lowercase letters into consideration.
The result of each operation is the index of the character in a 52 letters alphabet.
$$L8-018 → [A = 8], [B = 018 / 52 = 0], [C = 18 \\% 52 = 18]$$
$$8 0 18 → I A S$$
Thus "L8-018" becomes "IAS".
This way for each card in your deck you can encode their library_id. But once you encode it, even if you decode the string back to a library_id, there is no endpoint in the API to get a card by library_id.

## Built With 🛠
+ [Kotlin](https://kotlinlang.org/)
+ [Jetpack Compose](https://developer.android.com/jetpack/compose)
+ MVVM Architecture
+ [Retrofit2](https://github.com/square/retrofit)
+ [OkHttp3](https://github.com/square/okhttp)
+ [Timber](https://github.com/JakeWharton/timber)
+ [Coil](https://github.com/coil-kt/coil)
+ [DaggerHilt](https://developer.android.com/training/dependency-injection/hilt-android)
