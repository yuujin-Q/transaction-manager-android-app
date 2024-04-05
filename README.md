# Introduction

This is a template for doing Android development using GitLab
and [fastlane](https://fastlane.tools/).
It is based on the tutorial for Android apps in general that can be
found [here](https://developer.android.com/training/basics/firstapp/).
If you're learning Android at the same time, you can also follow along that
tutorial and learn how to do everything all at once.

# Getting started

First thing is to follow
the [Android tutorial](https://developer.android.com/training/basics/firstapp/) and
get Android Studio installed on your machine, so you can do development using
the Android IDE. Other IDE options are possible, but not directly described or
supported here. If you're using your own IDE, it should be fairly straightforward
to convert these instructions to use with your preferred toolchain.
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

## What's contained in this project

### Libraries

The state of this project is as if you followed the first few steps in the linked
[Android tutorial](https://developer.android.com/training/basics/firstapp/) and
have created your project. You're definitely going to want to open up the
project and change the settings to match what you plan to build. In particular,
you're at least going to want to change the following:

- Application Name: "My First App"
- Company Domain: "example.com"

### Reference links

- [GitLab CI Documentation](https://docs.gitlab.com/ee/ci/)
- [Blog post: Android publishing with GitLab and fastlane](https://about.gitlab.com/2019/01/28/android-publishing-with-gitlab-and-fastlane/)

You'll definitely want to read through the blog post since that walks you in detail
through a working production configuration using this model.

# Work distribution

| NIM      | Name                     | Work                                                                     | Total Time |
| -------- | ------------------------ | ------------------------------------------------------------------------ | ---------- |
| 13521067 | Yobel Dean Christopher   | Header-Navbar, Scan, Twibbon, Graph                                      | 69h        |
| 13521074 | Eugene Yap Jin Quan      | Login, Logout, JWT Service, Save Excel File, Send Email, Network Sensing | 69h        |
| 13521138 | Johann Christian Kandani | Transactions CRUD, Randomize Transactions                                | 69h        |
