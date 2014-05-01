# Wiki-Contacts Android

[Wiki-Contacts Android] is the android mobile client for web application [Wiki-Contacts]. 

The mobile app is available at [Google Play]. Repository of [Wiki-Contacts] is available at [GitHub].


## Searching contacts

- Parses JSON response from the web application using [Jackson] paser and displays into a list view. 

- Retrieves image using Intent.ACTION_GET_CONTENT and performs imaging processing before uploading to the web application.

- Displays app notifications using NotificationManager.

- Stores app data using SharedPreferences.


## Caller ID 

- Runs a background service (if enabled) to listen to incoming calls.

- Searches the phone book for the incoming call number for contact details. 

- If not found, searches the web application for the incoming call number for contact details.

- Uses WindowManager to display the contact details in a dialog over the incoming call screen. 


## License

[Wiki-Contacts Android] is released under version 2.0 of the [Apache License].


[Wiki-Contacts Android]: https://play.google.com/store/apps/details?id=com.kahkong.wikicontacts
[Wiki-Contacts]: http://wiki-contacts.com
[Google Play]: https://play.google.com/store/apps/details?id=com.kahkong.wikicontacts
[GitHub]: https://github.com/strengthandwill/wiki-contacts
[Jackson]: http://jackson.codehaus.org/
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0