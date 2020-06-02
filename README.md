# Perk by Daylight
An easy-to-use icon installer for the game [Dead by Daylight](https://store.steampowered.com/app/381210/Dead_by_Daylight/).
Inspired by [the subreddit with the same name](https://www.reddit.com/r/PerkByDaylight/).
<br /><br />

## How to Run

### Requirements

* Windows 7 / 8 / 8.1 / 10
* Java Runtime Environment [version 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)

### Running

Generally, most [releases](https://github.com/glitchedcoder/Perk-by-Daylight/releases) will also be shipped with a `run.bat` file.
This is a batch file that will run the program via the Command Line or whatever terminal you may have installed.
The `.JAR` file is not executable, so it must be ran with `java -jar FILENAME.jar` or `run.bat`.

### Optional Command Arguments

There are a list of arguments that is acceptable for Perk by Daylight.
* `-debug` This will output optional dialog to the console and is often used to "debug" the program. Very verbose.
* `-nocolor` This will disable coloring on the console and will make it harder to read. Perk by Daylight uses Jansi to output color.
## About

### Notes

This program is **not a hack client, crack, or exploit** and is not meant to be used while Dead by Daylight is launching or running.
This software complies with the Dead by Daylight and BHVR [End User License Agreement](https://store.steampowered.com/eula/381210_eula_0), but is in no way promoted, endorsed, or associated with Dead by Daylight, BHVR, or its licensors.
Improper use of this program could result in a false-positive on EasyAntiCheat's program and could get you falsely banned, just like manually installing icons could.
However, this is highly unlikely when used properly.
This program does not read, write, or modify game files, including game logs, in any way other than replacing `.PNG` files in their respective places.
There have been no reports of users getting banned for modifying or replacing in-game icons.

### Software Requirements

* Java [version 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) or higher.

That's it!

### Features

*Already Added*
* Dark and light theme toggle.
* Icon pack selector and icon preview.
* The ability to install an icon pack either in folder form or `.zip` form.
* An easy-to-use updater, with the option to ignore updates.
* Locally cached icon packs with their respective meta files, so no need to keep that `.zip` file in your downloads folder.
* The ability to rename an existing icon pack.
* The ability to see your currently installed icons.

*Plan to be Added*
* Edit/update already existing icon packs.
* Get detailed information of the selected icon pack.
* Multi-threaded workload support, as to not lag UI and for a cleaner experience.
* A built-in dialog box for those who want to donate.
* Lots of documentation that needs to be added.

*Might be Added*
* The support for `.rar` files. This might not be possible since RAR files are proprietary and libraries like [JUnRAR](https://github.com/junrar/junrar) do not support WinRAR-compressed files.
* A workshop for uploading, downloading, and viewing existing icon packs created by the Perk-by-Daylight community.
* More themes.

### Dependencies
Currently, Perk by Daylight uses the following libraries as dependencies:
* Google FindBugs v3.0.2
* [Google GSON](https://github.com/google/gson) v2.8.6
* [Google Guava](https://github.com/google/guava) v19.0
* [Jansi](https://github.com/fusesource/jansi) v1.17.1

Dependencies are automatically packed into the `.JAR` file and do not need to be downloaded.

## Screenshots

***Note:** these screenshots are pre-release and features can change*

*Dark and Light Mode Toggle* - Toggle between dark and light mode simply.
![Dark and Light Mode Toggler](https://i.imgur.com/VAbAIQY.gif)

*Easy Updater* - Updating with a click of a button.
![Easy Updater](https://i.imgur.com/BgYooBq.gif)

*Simple Renaming* - Double-click the icon pack to rename it.
![Simple Renaming](https://i.imgur.com/tE9Bgz6.gif)

## Frequently Asked Questions

**How can I be sure this isn't a hack?**

The full project, including the code and other resources, is open-sourced and is available for anyone to fork and modify to their heart's desire long as they stick to the [license](https://github.com/glitchedcoder/Perk-by-Daylight/blob/master/LICENSE.md). If you have questions about the what the code does or any other resources in this project, feel free to send me an e-mail at `glitchedcoder@gmail.com`.

**Where does the liability fall if someone were to be falsely banned?**

I have to be quite frank by saying that I do not take liability for the false banishment of any user using this program. This program in no way raises the chances of a user being banned and is the same likelyhood as another user manually installing icons. This program only automates the process and allows you to have collections of icon packs. The only way a user can be banned for changing in-game icons is when EasyAntiCheat changes their program to detect this.

**When will [insert feature] be added?**

I makes updates to this program on my own time and currently do not have a team of other developers working for me. If you have a feature that hasn't been previously suggested, feel free to create a feature request [here](https://github.com/glitchedcoder/Perk-by-Daylight/issues/new?assignees=glitchedcoder&labels=enhancement&template=feature_request.md&title=%5BFEATURE%5D).

**Where can I report a bug or error I've come across?**

If you come across a bug or error, feel free to report it [here](https://github.com/glitchedcoder/Perk-by-Daylight/issues/new?assignees=glitchedcoder&labels=bug&template=bug_report.md&title=%5BBUG%5D+). Thank you for your contribution to making Perk by Daylight as good as it can be. If you have an error log, which can be found by going to `File -> Open PBD Folder -> error logs`, please attach the file and follow the format of the bug report.

**I have a theme I think would look good. Can I add it to Perk by Daylight?**

Yes, actually. If you think you have a theme that's nice-looking and new, feel free to create a feature request [here](https://github.com/glitchedcoder/Perk-by-Daylight/issues/new?assignees=glitchedcoder&labels=enhancement&template=feature_request.md&title=%5BTHEME%5D). Feel free to use the current [dark theme](https://github.com/glitchedcoder/Perk-by-Daylight/blob/master/src/main/resources/theme/dark-theme.css) as a template. Make sure to give it a (not so long) name and, if accepted, you will be credited and it will be added to the program. Do note that the template is using JavaFX CSS and that there might not be an easy way to preview the theme.
