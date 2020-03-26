<div align="center">
  <a href="https://www.jhipster.tech/">
    <img src="https://github.com/jhipster/jhipster-artwork/blob/master/logos/JHipster%20RGB-small100x25px.png?raw=true">
    <p><h1>fullCalendar sample project</h1></p>
  </a>
</div>

# Introduction

This is a sample JHipster generated application using the [fullCalendar library](https://fullcalendar.io/docs/angular). It consists of different calendars which have different public and private events.

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

# Usage

To try out the features navigate to `/calendars`. To view existing events you may need to navigate to February 2020. Clicking on an event will open a modal window containing basic information about the event. You can create new calendars and events which will be displayed in the calendars page. It also supports multiple languages.

Everyone can see public events. To view private events you must be logged in as the user who has created them.

You can find the marketplace module generator [here](https://github.com/contribution-jhipster-uga/generator-jhipster-fullcalendar).

# To Do

- Optimisation between front and back interaction. Right now all the events are received once from the backend but it would be better if only the ones from the current chosen month or week are requested.
- Fix the bug of calendar header buttons (week, month, today) not changing language. See the commentaries in the `calendars.components.ts` file and [here](https://github.com/fullcalendar/fullcalendar/issues/4581).
- Add new features and interactions offered by fullCalendar API.
- Find a solution to the "id" variable in the `account.model.ts` which creates a problem with the generator but is needed to distinguish the current logged in user.
- Maybe add some style to the event modal to make it better.

# License

Apache-2.0 © [Contribution UGA](https://github.com/contribution-jhipster-uga)
