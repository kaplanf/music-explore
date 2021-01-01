# Music Explore - Itunes
Music Exploration app to search and save Artists, Albums, Tracks
Technologies implemented:

Architecture: MVVM

Dependency Injection: Dagger

Data: LiveData, ViewModel, ConstraintLayout

Persistence Layer: Room

Background Process: Coroutines

View: Navigation Component, Databinding

The app is developed in MVVM pattern

Repository instances are accessed through ViewModels
The data will always populated through DB throughout the app, the DB is the source of truth,
and it will be updated and observed through fetched data from Itunes API.

# Notes
The app saves results of your every search, ergo, if you lose your connection, the app will
load your previous results from the DB.

You can access to liked tracks from left menu, and listen to their previews.
