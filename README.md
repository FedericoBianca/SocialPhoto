# Overview
This is a project for the course of Mobile Application and Cloud Computing at la Sapienza, Rome.
The idea behind of this Application is to have whenever a person is a complete collection of the photo posted on the most important social network of our days: Facebook. The project is mainly centered on the usage of the Open Graph API offered by Facebook to make particular queries on it.

# Front-End
The application is structured with a MainActivity that checks if a user is logged or not:
* Not Logged: the Main Activity redirects with an Intent to the Login Activity in which is **required** to sign in in order to use the app.
* Logged: in this case the Main Activity redirects immediately to the TabActivity, which is the *core* of the app.

## Tab Activity
This Activity is composed by two **Fragments** :

1. **Search Tab**: this part is composed of 1 ImageView, 1 TextView, 2 spinners, 1 fragment and 1 button: 
* The ImageView, that is a circleImageView (taken by a repository in github), and the TextView contain respectively the Profile Picture and the Username of Facebook's logged user, through the usage of the Graph API that requires the ID and the AccessToken of the user taken previously in the Login Activity and saved permanently in the sharedPreferences.
* The 2 spinners contains respectively the User's Albums on Facebook, taken with a method that requires user's ID, and the years that he/she can choose.
* The Fragment is called PlaceAutocompleteFragment and it's used to make predictions about the cities searched by the user (this is a service offered by Google and requires a Google API key to work).
* The button, called Submit, used to start the Display Photos Activity (we talk about it later).

2. **Overview Tab**: this part is composed by two spinners and one button:
* The spinners contain respectively the years and month that the user can choose.
* The button that performs the same action in the Search Tab but with different parameters and starts an Intent to the Recap Activity.

## Display Photos Activity
This activity is centered to display all the photos in gridView returned by the method *getFacebookPhoto()*, based on a synchronous query on Facebook with the Album, Year and Location selected by the user, and the grid is automatically filled with a custom adapter that takes an ArrayList of String and every string is converted into a Bitmap with the Picasso library; the choice of Picasso is based on the fact that the usage of the system's resources are optimized and the images are displayed in a really smoothy way. In order to avoid the freeze of the Main thread, these part is all performed in an AsyncTask that fill asynchronously the gridView as soon as the images are processed by Picasso and shows to the user an indeterminate ProgressBar to warn him/her about the loading of the photos. It's also possible to click on the desired photo in order to see it in fullview and this thanks to the fact that when the photo is clicked, the activity starts an Intent targeting the Full Image Activity that displays the photo inside an ImageView that fills all the Activity's layout.

## Recap Activity
This Activity is composed by a ListView that, for every item of the list, there is an ImageView and a TextView and this thanks to a custom layout called *mylist.xml* passed in a custom adapter, that takes two String Vectors, to fill all the items inside the list itself.
It represents the most liked photo and most commented photo given a certain year and month; besides it focuses on the number of likes and the number of the comments of the photos.

## Other Aspects
The Front-End part is mainly focused on the algorithm behind it and in particular in the fetching of the Photos and Album's names on Facebook; the difficulty is based on the parsing of the JSONObject returned by the GraphRequest because of the errors that can be occur while the working thread fetches the data or because there can be incongruences on the JSONObject itself: I want to stress the fact that the Place AutoComplete Fragment, when a city is chosen, returns the name of the city in the locale language of the device while the city field in the JSONObject returned by the GraphRequest is always in English, so it was necessary to make another step in order to have a congruence between the city's name chosen in the Fragment and the one returned by Facebook; this it was possible by the usage of the Geocoder class that takes the latitude and longitude of the city selected by the user and converts them in an Address with a Locale in English in order to retrieve the cities' names matching with the Facebook returned ones. 
Another important aspect is the avoidance of the uncontrolled Exceptions that cause the crash of the App and I put a lot of try/catch blocks, in particular on those parts that require a connection to the Back-End or to the Graph API which are simply  based on HTTP request that can be unpredictable for many reasons: for this reason I use the Connectivity Manager Service in order to see if the device is connected or not to Internet because is useless/dangerous to make queries if the device is offline. Last but not least, in both Display Photos and Recap Activities there is a particular layout called Swipe-to-Refresh used to refresh the screen with a swipe-down gesture: this because there can be some wrong behaviours while fetching data, so the user can simply make a gesture and try to fetch the same data again without refilling the fields in the calling activities.

 
