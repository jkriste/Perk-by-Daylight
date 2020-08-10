# Perk by Daylight

An easy-to-use icon installer for the game [Dead by Daylight](https://store.steampowered.com/app/381210/Dead_by_Daylight/).
Inspired by [the subreddit with the same name](https://www.reddit.com/r/PerkByDaylight/).

## Table of Contents

* [How to Run](https://github.com/glitchedcoder/Perk-by-Daylight#how-to-run)
  * [Requirements](https://github.com/glitchedcoder/Perk-by-Daylight#requirements)
  * [Running](https://github.com/glitchedcoder/Perk-by-Daylight#running)
* [About](https://github.com/glitchedcoder/Perk-by-Daylight#about)
  * [Notes](https://github.com/glitchedcoder/Perk-by-Daylight#notes) (please read before using)
  * [Features](https://github.com/glitchedcoder/Perk-by-Daylight#features)
  * [Dependencies](https://github.com/glitchedcoder/Perk-by-Daylight#dependencies)
* [Screenshots](https://github.com/glitchedcoder/Perk-by-Daylight#screenshots)
* [Frequently Asked Questions](https://github.com/glitchedcoder/Perk-by-Daylight#frequently-asked-questions)

## How to Run

### Requirements

* Windows OS 7 / 8 / 8.1 / 10
* Java Runtime Environment [version 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) or higher.

### Running

To download Perk by Daylight, either go to the `Releases` tab on the right side of the screen or click [this link](https://github.com/glitchedcoder/Perk-by-Daylight/releases). Go to the latest version, optionally review the release notes, click `Assets`, and then click `PerkByDaylight.jar`.

Generally, most [releases](https://github.com/glitchedcoder/Perk-by-Daylight/releases) will also be shipped with a `run.bat` file.
This is a batch file that will run the program via the Command Line or whatever terminal you may have installed.
The `.JAR` file is not executable, so it must be ran with `java -jar PerkByDaylight.jar` in the command line or double-clicking the `run.bat` file.

If you get a pop-up when double-clicking the `run.bat` file, click `More Info` and then `Run Anyway`.

## About

### Notes

This program is **not a hack client, crack, or exploit** and is not meant to be used while Dead by Daylight is launching or running.
This software complies with the Dead by Daylight and BHVR [End User License Agreement](https://store.steampowered.com/eula/381210_eula_0), but is in no way promoted, endorsed, or associated with Dead by Daylight, BHVR, or its licensors.
Improper use of this program could result in a false-positive on EasyAntiCheat's program and could get you falsely banned, just like manually installing icons could, [as noted by a BHVR community manager](https://forum.deadbydaylight.com/en/discussion/comment/452675/#Comment_452675).
However, this is highly unlikely when used properly.
This program does not read, write, or modify game files, including game logs, in any way other than replacing `.PNG` files in their respective places.
There have been no reports of users getting banned for modifying or replacing in-game icons.

### Features

*Already Added*
* Dark and light theme toggle.
* Icon pack selector and icon preview.
* The ability to install an icon pack either in folder form or `.zip` form.
* An easy-to-use updater, with the option to ignore updates.
* Locally cached icon packs with their respective meta files, so no need to keep that `.zip` file in your downloads folder.
* The ability to rename an existing icon pack.
* The ability to see your currently installed icons.
* Packs are re-evaluated every launch in case files were messed with while closed.
* Multi-threaded workload support, as to not lag UI and for a cleaner experience.
* A UI-implementation of preferences.
* Logs being created and stored individually once per run, in its own `logs` folder.
* A built-in dialog box for those who want to donate.

*Plan to be Added*
* The ability to create an empty icon pack and add icons to it.
* The ability to take an existing icon pack and ZIP it to a specific folder.
* Edit/update already existing icon packs.
* Get detailed information of the selected icon pack.
* Lots of documentation that needs to be added.
* Updating an old icon with a new icon.

*Might\* be Added*
* An option to revert to the default Dead by Daylight icons. Saves the hassle of going through Steam and verifying game files.
* The support for `.rar` files. This might not be possible since RAR files are proprietary and libraries like [JUnRAR](https://github.com/junrar/junrar) do not support WinRAR-compressed files.
* A workshop for uploading, downloading, and viewing existing icon packs created by the Perk-by-Daylight community.
* More themes. See [FAQ](https://github.com/glitchedcoder/Perk-by-Daylight#frequently-asked-questions).

### Dependencies

Currently, Perk by Daylight uses the following libraries as dependencies:
* Google FindBugs v3.0.2
* [Google GSON](https://github.com/google/gson) v2.8.6
* [Jansi](https://github.com/fusesource/jansi) v1.17.1
* [OkHttp](https://github.com/square/okhttp) v4.7.2

Dependencies are automatically packed into the `.JAR` file and do not need to be downloaded.

## Screenshots

***Note:** these screenshots were taken at previous versions and may no longer represent the feature in the current version.*

*Dark and Light Mode Toggle* - Toggle between dark and light mode simply.
![Dark and Light Mode Toggler](https://i.imgur.com/VAbAIQY.gif)

*Easy Updater* - Updating with a click of a button.
![Easy Updater](https://i.imgur.com/BgYooBq.gif)

*Simple Renaming* - Double-click the icon pack to rename it.
![Simple Renaming](https://i.imgur.com/tE9Bgz6.gif)

*Easy Clearing of Cache* - Go to `File -> Delete Cache` to delete all contents in the `temp` folder.
![Easy Clearing of Cahce](https://i.imgur.com/2DSCJq7.gif)

*Easily Change Preferences* - Go to `File -> Preferences...` to change how the program operates.
![Easily Change Preferences](https://i.imgur.com/GnaiLeK.gif)

*Get Information About Icon Packs* - Go to `Pack -> Icon Pack Info...` to get information like number of icons, icon pack location, and missing icons.
![Get Information About Icon Packs](https://i.imgur.com/3vXLNjF.gif)

## Frequently Asked Questions

**How can I be sure this isn't a hack?**

The full project, including the code and other resources, is open-sourced and is available for anyone to fork and modify to their heart's desire as long as they stick to the [license](https://github.com/glitchedcoder/Perk-by-Daylight/blob/master/LICENSE.md). If you have questions about what the code does or any other resources in this project, feel free to create an issue with the `question` label or send me an email at `glitchedcoder[at]gmail.com`.

**Where does the liability fall if someone were to be falsely banned?**

I have to be quite frank by saying that I do not take liability for the false banishment of any user using this program. This program in no way raises the chances of a user being banned and is the same likelyhood as another user manually installing icons. This program only automates the process and allows you to have collections of icon packs. The only way a user can be banned for changing in-game icons is when EasyAntiCheat changes their program to detect this, which is not likely for the foreseeable future.

**How do I create a Perk-by-Daylight-compatible .ZIP file?**

As long as your icon pack has at least one of the following sub-folders (shown below) all that's needed is to go to your main icon pack folder (that contains folders like `Actions`, `Perks`, etc.), right-click it, then select `Send to -> Compressed (zipped) folder`.

![Layout](https://i.imgur.com/xkG84eC.png)

If the icon pack you downloaded does not have a similar layout or is a `.rar` file, just extract it and go to `Pack -> New Icon Pack -> From Folder` and select the main pack folder (containing `Actions`, `Perks`, etc).

**Where can I go to get a pre-made icon pack?**

There are plenty of sources where icon packs are available to download. Check out some of the links below:
* [Reddit (r/PerkByDaylight)](https://www.reddit.com/r/PerkByDaylight/)
<!--- TODO -->

**When will [insert feature] be added?**

I make updates to this program on my own time and currently do not have a team of other developers working for me. If you have a feature that hasn't been previously suggested, feel free to create a feature request [here](https://github.com/glitchedcoder/Perk-by-Daylight/issues/new?assignees=glitchedcoder&labels=enhancement&template=feature_request.md&title=%5BFEATURE%5D).

<i>* Features that are in the "Might be Added" category are quite trivial and would require hacky workarounds or months and months of development, and as such, probably won't see the light of day.</i>

**How can I contribute to Perk by Daylight?**

Any help with the project is much appreciated! If you have experience with Java or JavaFX, feel free to fork the project and make a pull request once you're satisfied with the changes. If the pull request is accepted, I will be sure to give you credit. Thank you for your contribution!

Optionally, if you're not a developer, but would like to contribute, you can make a donation [here](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=kristensenjl%40my.gvltec.edu&item_name=Making+free+software+for+all.&currency_code=USD&source=url). Donations are optional and any amount is helpful. Thank you!

**Where can I report a bug or error I've come across?**

If you come across a bug or error, feel free to report it [here](https://github.com/glitchedcoder/Perk-by-Daylight/issues/new?assignees=glitchedcoder&labels=bug&template=bug_report.md&title=%5BBUG%5D+). Thank you for your contribution to making Perk by Daylight as good as it can be. If you have an error log, which can be found by going to `File -> Open PBD Folder -> error logs`, please attach the file and follow the format of the bug report.

**I have a theme I think would look good. Can I add it to Perk by Daylight?**

Yes, actually. If you think you have a theme that's nice-looking and new, feel free to create a feature request [here](https://github.com/glitchedcoder/Perk-by-Daylight/issues/new?assignees=glitchedcoder&labels=enhancement&template=feature_request.md&title=%5BTHEME%5D). Feel free to use the current [dark theme](https://github.com/glitchedcoder/Perk-by-Daylight/blob/master/src/main/resources/theme/dark-theme.css) as a template. Make sure to give it a (not so long) name and, if accepted, you will be credited and it will be added to the program. Do note that the template is using JavaFX CSS and that there might not be an easy way to preview the theme.

**What data is cached on my computer?**

All data that Perk by Daylight caches on your computer can be found by going to `File -> Open PBD Folder` or typing in the Windows search bar `%appdata%` then selecting the `Perk by Daylight` folder. There, you can find files such as `latest.log` which stores the current log if the program is running, `config.json`, which stores your preferences, and three folders:
* `packs` - This folder stores all icon packs. All icon packs contain a `packmeta.json` file which caches the name and the missing icons of the pack. This is updated every time you open the program.
* `error logs` - This folder stores any and all occurrences of errors and keeps the diagnostics in the file, formatted `XXXX.XX.XX.XX.XX.XX.XXX.log`. The X's represent the time the error occurred, up to the millisecond. Error logs **do not** store any personal information about you or your computer, only what was going on when the error happened and the error itself.
* `logs` - This folder stores all logs of previous runs of the program, formatted `XXXX.XX.XX.XX.XX.XX.log`. The X's represent the time the log was saved, up to the second.
* `temp` - This folder is used to evaluate new icon packs before they are added to the `packs` folder. If the new pack is evaluated to not be an icon pack, the icon pack folder and its contents will remain in the `temp` folder until deleted.

You can optionally delete all logs, error logs, and anything in the `temp` folder by checking `Delete Logs` and `Delete Error Logs` in your preferences and then going to `File -> Delete Cache`.

**What about privacy? Is any of my data being collected?**

Perk by Daylight will in no circumstances upload **ANY** data. The only time Perk by Daylight will use the network is when downloading an update when requested to. You have the option of ignoring updates by going to `File -> Preferences...` and checking the `Ignore Updates` option. Optionally, there is also a `Offline Mode` option, in which Perk by Daylight can be used without internet and cannot check for updates. I respect the privacy of anyone that uses Perk by Daylight and can guarantee that Perk by Daylight does not use the internet with the exception of downloading updates.

**Why Java of all programming languages?**

The first language I learned was Java, and I've been slowly getting better at it for the past 5 years. There are many languages that are better suit for this kind of project, but this was the one I was most experienced with.

**When did you first start working on Perk by Daylight?**

I first had the idea around the time quarantine first started, around early to mid March. Dead by Daylight is one of my favorite games and I thought this would be a good way to contribute to the community.