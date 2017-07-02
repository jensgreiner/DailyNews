# DailyNews
Udacity Android Basiscs Nanodegree (EU) Final project for Networking &amp; JSON lessons

# News App

## Project Overview
The final project is a chance for you to combine and practice everything you learned in this section of the Nanodegree program. You will be making your own app that connects to the Internet to provide news articles on a topic of your choice.

The goal is to create a News feed app which gives a user regularly-updated news from the internet related to a particular topic, person, or location. The presentation of the information as well as the topic is up to you.

### Why this project?
In the most recent portion of the Nanodegree program, you worked to build the Earthquake app. Along the way, you learned about connecting to the internet in Android, parsing responses from an API, updating the information in your app, and properly displaying that information. Practicing these skills is imperative in order to build apps that delight and surprise users by anticipating their needs and supplying them with relevant information.

### What will I learn?
This project is about combining various ideas and skills we’ve been practicing throughout the course. They include:

Connecting to an API
* Parsing the response of the API
* Handling error cases gracefully
* Updating information regularly
* Doing network operations independent of the Activity lifecycle

## Build Your Project
To Learn about the Guardian API, take a look at [this page](http://open-platform.theguardian.com/documentation/). Also, try modifying [this example query](http://content.guardianapis.com/search?q=debates&api-key=test) or [this example query](http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test).

Your project will be evaluated using the News App project rubric.

### Additional Criteria
The intent of this project is to give you practice writing raw Java code using the necessary classes provided by the Android framework; therefore, the use of external libraries will not be permitted to complete this project.

---

The app uses the api with a fixed setting of query parameters and is retrieving only news from the current day.
The list view is implemented by using a `RecyclerView` including `ViewHolder`, an `AsyncTaskLoader` and a `SwipeRefreshLayout`.

#### Screenshots

<img src="https://dl.dropboxusercontent.com/s/lkz231d5xyvidgq/Screenshot_1499017721.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/fgkscnbes9dw2jy/Screenshot_1499017726.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/pomhg88z1z1fyj4/Screenshot_1499018025.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/nc3bwip3x779ofq/Screenshot_1499018073.png" alt="" width=200/>

