![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Android Studio](https://img.shields.io/badge/android%20studio-346ac1?style=for-the-badge&logo=android%20studio&logoColor=white)
## Flickr Search

**Intro:  
Flickr Search is an infinitely scrolling image search native Android app built with Kotlin and Jetpack and Compose.

**About:  
utilizes the following 3rd Party Libs: Coil (Image Loader), Hilt (DI), Flickr SDK

**Design:  
MVVM architecture. Both the Search and Detail Pages are Navigation Destinations linked to Hilt managed Viewmodels which expose observable
MutableStateFlows containing uiState objects. The ViewModels themselves share a common Flickr SDK singleton component injected via Hilt.


<img width="445" alt="Screenshot 2024-04-16 at 9 03 51 AM" src="https://github.com/ajsan90/android_flickr_client/assets/153450151/85b79337-0922-45aa-995d-86af000f727e">

