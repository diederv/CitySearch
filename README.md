# CitySearch

This Android App creates a set of in-memory indices at startup to allow for a fast lookup by a search-term.
A simple linear search would not perform very well since the total number of cities that it needs to include is pretty high (it contains 209556 cities). 

For example, if the following small set of cities needs to be indexed:

|rowNum|City|
|--:|---|
|0|Aalsmeer|
|1|Abbekerk 1|
|2|Abbekerk 2|
|3|Abbekerk 3|
|4|Abcoude 1|
|5|Abcoude 2|
|6|Amsterdam|
|7|Amstelveen|

then the **IndexService** wil create 4 indices as follows:

index 1:  
a -> pointer to line: 0 .

index 2:   
aa -> pointer to line: 0 .  
ab -> pointer to line: 1 .  
am -> pointer to line: 6 .  

index 3:  
aal -> pointer to line: 0 .  
abb -> pointer to line: 1 .  
abc -> pointer to line: 4 .  
ams -> pointer to line: 6 .  

index 4:  
aals -> pointer to line: 0 .  
abbe -> pointer to line: 1 .  
abco -> pointer to line: 4 .  
amst -> pointer to line: 6 .  

Besides these indices, the IndexService will create a RandomAccessFile that contains all the city in alphabetical order. 
The pointers mentioned in the example-indices refer to the first occurance of a matching city, and because the cities are ordered alphabetically, the other matching cities (if any) will occur sequentially, The **SearchService** will, in case of a user-query, retrieve these follow-up matching cities.


**Constrains and Requirements**

The Android Studio project that forms the basis of the Android App, should be compliant with all the Constrains and Requirements of the assessment.

- No Beta version of Android Studio (used version: 3.5)
- Only 3rd party lib used: Gson
- One Activity with a few Fragments
- Responsive: Different latout for landscape orientation
- The UI is responsive in the sense that it respons to every key pressed and it is fast
- The UI should look as suggested
- It contains tests for the IndexServices and for the SearchService
- It contains an UI EspressoTest 



## Screenshots:

The Splash screen (while creating the indices)
![splash_screen](https://user-images.githubusercontent.com/2026484/67287915-4a6ab380-f4dc-11e9-9957-e3a28f2b3a0b.png)

The search screen (portrait)
![search_screen](https://user-images.githubusercontent.com/2026484/67287912-49d21d00-f4dc-11e9-86b8-32b755ac6a7e.png)

Selecting a city brings you to the google-map with that city
![map_screen](https://user-images.githubusercontent.com/2026484/67287914-4a6ab380-f4dc-11e9-86f8-8982b07b7294.png)

Going back to the search result with the list of cities, and clicking on the info-button shows:
![city_info_screen](https://user-images.githubusercontent.com/2026484/67287913-4a6ab380-f4dc-11e9-9faa-f38c81617b09.png)


And in landscape mode:
![splash_screen_landscape](https://user-images.githubusercontent.com/2026484/67287919-4b034a00-f4dc-11e9-951c-94e9c291e8fb.png)

![search_map_screen_landscape](https://user-images.githubusercontent.com/2026484/67287920-4b034a00-f4dc-11e9-9671-b821582e09d4.png)

![city_info_screen_landscape](https://user-images.githubusercontent.com/2026484/67287917-4b034a00-f4dc-11e9-92fb-82b79f6106bf.png)



## Problem in Android Studio 3.5

If you run into the following problem with Android Studio (3.5)
<img width="785" alt="android_studio_problem" src="https://user-images.githubusercontent.com/2026484/67289040-3b850080-f4de-
11e9-8bda-f007f5eb0a7a.png">

Then you can solve that by updating this versin of Android Studio to version 3.5.1
(Bij using the option: "Check for Updates")



Best Regards,
Diederick Verweij
