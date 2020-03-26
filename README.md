<div align="center">
  <a href="https://www.jhipster.tech/">
    <img src="https://github.com/jhipster/jhipster-artwork/blob/master/logos/JHipster%20RGB-small100x25px.png?raw=true">
    <p><h1>ical4j library for the fullCalendar module</h1></p>
  </a>
</div>

# Introduction

This is a feature branch for the fullCalendar module. You can find the base sample project on the [master branch](https://github.com/contribution-jhipster-uga/sample-jhipster-fullcalendar) and more information about the marketplace fullcalendar generator [here](https://github.com/contribution-jhipster-uga/generator-jhipster-fullcalendar).

It includes the ical4j java library which helps with ics  agenda file manipulation following the ical convetion. More information about the ical4j library can be found in the following links: [github repo](https://github.com/ical4j/ical4j), [Home wiki](https://github.com/ical4j/ical4j/wiki), [Examples](https://github.com/ical4j/ical4j/wiki/Examples).

# Installation

Just pull this repo in your local forlder of choice using git and launch the maven compiler

```bash
git init
git remote add origin https://github.com/contribution-jhipster-uga/sample-jhipster-fullcalendar.git
git checkout -b feature/ical4j
git fetch --all
git pull origin feature/ical4j

./mvnw
```

Sometimes the compiler fails to build the project. Try to delete the `target/elasticsearch` folder or do a `./mvnw clean` and see if that works.

# Usage

Please read the master branch usage before this one.

To try out the features navigate to `/calendars` page and click on the `ical` button dropdown. You can either export the current events as an ics file or import events from an ics file.

## Export

To export your agenda click on `Export Agenda`. A new file named `agenda_export.ics` will appear on the root directory of the project. All the events in the database will be put in the file.

### To Do

- **Add export options** : Instead of saving all the events, filter the events to download based on a time period (month, week, day) and an ownership (logged in user or checked calendar).

- **Normal download** : In deployment, the created file needs to be sent back to the front end so that it can be downloaded by the user and saved in his download folder.

- **Error / Success notification**

## Import

To export your agenda click on `Import Agenda`. An ics file named `ADECal.ics` must be present in the root folder for the import to work. A new calendar will be created in which the new events will be stored.

### To Do

- **Upload Interface** : In deployment, the user should choose from its directory the ics file to upload.

- **Check for duplicates** : Verify if the exact event or calendar already exists. Right now, if you upload the same ics file, new exact events and calendar will be created.

- **Error / Success notification**

# License

Apache-2.0 © [Contribution UGA](https://github.com/contribution-jhipster-uga)