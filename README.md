# CitySearch

This Android App creates a set of in-memory indices at startup to allow for a fast lookup by a search-term.
A simple linear search would not perform very well since the total number of cities is pretty high (it contains 209556 cities). 

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

then the **IndexService** wil create 4 indices like follows:

index 1:
a -> pointer to line: 0

index 2:
aa -> pointer to line: 0
ab -> pointer to line: 1
am -> pointer to line: 6

index 3:
aal -> pointer to line: 0
abb -> pointer to line: 1
abc -> pointer to line: 4
ams -> pointer to line: 6

index 4:
aals -> pointer to line: 0
abbe -> pointer to line: 1
abco -> pointer to line: 4
amst -> pointer to line: 6

Besides these indices, the IndexService will create a RandomAccessFile that contains all the city in alphabetical order. 
The pointers mentioned in the example indices refer to the first occurance of a matching city, and because the cities are ordered alphabetically, 
the sequantial matches will occur right after, The **SearchService** will, in case of a user-query retrieve these follow-up matching cities.

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


Regards,
Diederick Verweij