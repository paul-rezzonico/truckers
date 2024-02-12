# TRUCKERS

## Overview

<table>
  <tr>
    <td>
      <img alt="Latice DxD Logo" src="app\src\main\res\truckers-logo.png" width="400">
    </td>
    <td>
      <p>Truckers is an Android service that tracks messages from truck drivers related to their routes, appointments, deliveries, etc. The service retrieves messages received by drivers while filtering out message from invlaids sources. The service then save messages before synchronizing them in a REST API to have a history of the messages received on the Admin side.</p>
    </td>
  </tr>
</table>

## Table of contents

- [Overview](#overview)
- [Table of contents](#table-of-contents)
- [Features](#features)
   - [Exemple of Valid Messages](#exemple-of-valid-messages)
   - [Exemple of Invalid Message](#exemple-of-invalid-message)
- [Installation](#installation)
  - [Configuration](#configuration)
  - [Admin Configuration](#admin-configuration)
  - [Message Storage](#message-storage)
- [Technologies used](#technologies-used)
- [RNCP referential](#rncp-referential)

## Features

- **Receive Messages:** The service automatically retrieves messages received by drivers.

- **Filter sender:** The service filters out messages from invalid sources.
- **Filter messages:** The service filters out invalid messages, and stores valid and invalid messages in separate files.
- **Manage Messages:** The stored messages are synchronized in a REST API to have a history of the messages received on the Admin side.

### Exemple of Valid Messages

- **Appointment :** 
```	
RDV prévu le 4 janvier à 14h
```

### Exemple of Invalid Message

- **Invalid Message :** 
```
Bonjour, pouvez-vous m’indiquer votre position ? Pouvez-vous me répondre maintenant ?
```

## Installation

1. Clone the repository.

```bash
git clone  https://github.com/paul-rezzonico/truckers.git
```

2. Open the project in Android Studio.

3. Run the project on an Android device.

### Configuration

To use the application, you need to give the application the necessary permissions to access the storage and SMS. You also need to provide a default file to the application.

1. Open a file manager on your phone.

2. Go to the downloads directory.

3. Place the defaut.json file in the directory.

4. Launch the application, after validating the permissions for storage and SMS, the service should start and create a logcat.txt file in the same directory as the default file.

The "defaut.json" file contains the default configurations necessary for the proper functioning of the application. Be sure not to inappropriately modify this file to avoid any configuration problems. You can restart the application, the service will start and remain active.

### Admin Configuration

Administrators can configure the application using specific configuration messages.

- **Add Number:**  
```
CONFIG Ajout numéro 011111111111
```
- **Delete Number:** 
```
CONFIG Suppression numéro 011111111111
```
- **Add Keyword:** 
```
CONFIG Ajout mot-clé RENDEZ-VOUS
```
- **Delete Keyword:**
```
CONFIG Suppression mot-clé RENDEZ-VOUS
```
- **Add Administrator:**
```
CONFIG Ajout administrateur 023923492
```
- **Delete Administrator:**
```
CONFIG Suppression administrateur 023923492
```

### Message Storage

Messages are stored in the directory `/data/data/com.unilim.iut.truckers/files`. They are divided into two separate files based on their validity. Each day, new files are created to minimize the risk of information loss.

1. Open the device explorer in Android Studio.

2. Go to the directory `/data/data/com.unilim.iut.truckers/files`.

3. You will find the files `messagesValides-date` and `messageInvalides-date`.

4. To view the messages, open the files with a text editor.

## Technologies used
- **Kotlin:** ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
- **Android:** ![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)

## RNCP referential

| Skill | Description | How it's used in the project |
| :---: | :---------: | :--------------------------: |
| **![RNCP35475BC01](https://img.shields.io/badge/RNCP35475BC01-3-00BFFF?style=flat)** | Perform an application development | With this project, I was able to develop a Java application from scratch. I used the JavaFX library to create the GUI and implemented the game logic in Java. |
| **![RNCP35475BC02](https://img.shields.io/badge/RNCP35475BC02-3-00BFFF?style=flat)** | Optimize IT applications | I used Test and Debugging techniques to ensure the application runs smoothly. |
| **![RNCP35475BC03](https://img.shields.io/badge/RNCP35475BC03-3-00BFFF?style=flat)** | Administer communicating computer systems | I used the Android API to communicate with the phone's SMS system. |
| **![RNCP35475BC04](https://img.shields.io/badge/RNCP35475BC04-3-00BFFF?style=flat)** | Manage information data | I used the Android API to manage the storage of the messages. I also used the JSON format to store the configuration of the application.
| **![RNCP35475BC05](https://img.shields.io/badge/RNCP35475BC05-3-00BFFF?style=flat)** | Lead a project | I worked with a team as a scrum master to develop this project. We used the Agile methodology and youtrack to manage the project.
| **![RNCP35475BC06](https://img.shields.io/badge/RNCP35475BC06-3-00BFFF?style=flat)** | Working in an IT team | I worked with a team to develop this project. |
| **![RNCP35475BC07](https://img.shields.io/badge/RNCP35475BC07-3-00BFFF?style=flat)** | Uses of digital tools | I used youtrack to manage the project. |
| **![RNCP35475BC08](https://img.shields.io/badge/RNCP35475BC08-3-00BFFF?style=flat)** | Use of data for analysis | I used the android API to ckeck the validity of incoming messages. |

----

Thank you for reading this far. This project is supposed to work with the ReST API project. You can find it [here](https://github.com/paul-rezzonico/truckers_api/tree/main).

