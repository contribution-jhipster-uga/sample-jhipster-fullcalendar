<div align="center">
  <a href="https://www.jhipster.tech/">
    <img src="https://github.com/jhipster/jhipster-artwork/blob/master/logos/JHipster%20RGB-small100x25px.png?raw=true">
    <p><h1>fullCalendar sample project</h1></p>
  </a>
</div>

# FullCalendar

## Introduction

This is a sample JHipster generated application using the [fullCalendar library](https://fullcalendar.io/docs/angular). It consists of different calendars which have different public and private events.

## Installation

Just pull this repo in your local forlder of choice using git and launch the maven compiler

## Usage

To try out the features navigate to `/calendars`. To view existing events you may need to navigate to February 2020. Clicking on an event will open a modal window containing basic information about the event. You can create new calendars and events which will be displayed in the calendars page. It also supports multiple languages.

Everyone can see public events. To view private events you must be logged in as the user who has created them.

You can find the marketplace module generator [here](https://github.com/contribution-jhipster-uga/generator-jhipster-fullcalendar).

## To Do

- Optimisation between front and back interaction. Right now all the events are received once from the backend but it would be better if only the ones from the current chosen month or week are requested.
- Fix the bug of calendar header buttons (week, month, today) not changing language. See the commentaries in the `calendars.components.ts` file and [here](https://github.com/fullcalendar/fullcalendar/issues/4581).
- Add new features and interactions offered by fullCalendar API.
- Find a solution to the "id" variable in the `account.model.ts` which creates a problem with the generator but is needed to distinguish the current logged in user.
- Maybe add some style to the event modal to make it better.

# ICal4j

## Introduction
ICal4j is a java library for parsing and building iCalendar data models. It helps with ics agenda file manipulation following the ical convetion. More information about the ical4j library can be found in the following links: [github repo](https://github.com/ical4j/ical4j), [Home wiki](https://github.com/ical4j/ical4j/wiki), [Examples](https://github.com/ical4j/ical4j/wiki/Examples).

## Usage
To try out the features navigate to `/calendars` page and click on the `ical` button dropdown. You can either export the current events as an ics file or import events from an ics file. You can find tutorial videos [here](https://drive.google.com/open?id=1Iea4yBwePS1FVcW_zDaZHjsEbxya6ya_).

### Import
To import an ics file you must have an account and be logged in. The imported events will be private to the logged in account. If wanted, this can be easily changed in the entities page.

### Export
You can export the events as an ics file. The number of events exported depends on the time view: day, week or month. Everyone can export events, but to download non public events you must be logged in as the user who created them.

# License

Apache-2.0 © [Contribution UGA](https://github.com/contribution-jhipster-uga)
