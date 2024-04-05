# Introduction

Bandung Bondowoso's transaction management app. The app is based on Android's platform. The dream app that can help record and manage your transaction! The app is made by genies at Genie Institute.

Bondoman helps users manage transactions with its adding, editing, and deleting transactions. The app also comes with various utilities (not so) related to the main transaction functionality

# Screenshots
![alt text](screenshots/308507.jpg)
![alt text](screenshots/308512_0.jpg)
![alt text](screenshots/308513_0.jpg)
![alt text](screenshots/308514_0.jpg)
![alt text](screenshots/308515_0.jpg)
![alt text](screenshots/308516_0.jpg)
![alt text](screenshots/308517.jpg)
![alt text](screenshots/308518.jpg)
![alt text](screenshots/308547_0.jpg)
![alt text](screenshots/308548_0.jpg)
![alt text](screenshots/308551_0.jpg)
![alt text](screenshots/308552_0.jpg)
![alt text](screenshots/308554_0.jpg)
![alt text](screenshots/308555_0.jpg)
![alt text](screenshots/308556_0.jpg)
![alt text](screenshots/308557_0.jpg)
![alt text](screenshots/308558_0.jpg)
![alt text](screenshots/308559_0.jpg)
![alt text](screenshots/308560_0.jpg)
![alt text](screenshots/308561_0.jpg)
![alt text](screenshots/308562_0.jpg)
![alt text](screenshots/308563.jpg)

# Accessiblity Check
Code inspection on Android Studio shows the following accessibility issue, which is the missing alt-text on dialog_image_preview.
![Alt text missing](accessibility/image.png)

Problem lies on this highlighted part of ImageView tag.
![alt text missing](accessibility/image-1.png)

Quick fix for this issue is as shown below.
![fixed alt text](accessibility/image-2.png)

# What's contained in this project

## Libraries

The application uses several libraries:

* Room - manages data persistence in database & providing ORM for ease
* Retrofit & OkHttp - helps manage http requests
* Moshi & Gson - .json data parsing for http requests
* Android Play Services (Location) - location providing utility
* Dagger Hilt - helps inject dependencies for nested modules
* Apache POI - helps build `.xls` & `.xlsx` files
* MPAndroidChart - helps create graphs


### Reference links

- [MPAndroidChart by PhilJay](https://github.com/PhilJay/MPAndroidChart)

# Work distribution

| NIM      | Name                     | Work                                                                     | Total Time |
| -------- | ------------------------ | ------------------------------------------------------------------------ | ---------- |
| 13521067 | Yobel Dean Christopher   | Header-Navbar, Scan, Twibbon, Graph                                      | 69h        |
| 13521074 | Eugene Yap Jin Quan      | Login, Logout, JWT Service, Save Excel File, Send Email, Network Sensing | 69h        |
| 13521138 | Johann Christian Kandani | Transactions CRUD, Randomize Transactions                                | 69h        |
