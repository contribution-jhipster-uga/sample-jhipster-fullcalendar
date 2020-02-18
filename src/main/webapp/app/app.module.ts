import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { FullCalendarSharedModule } from 'app/shared/shared.module';
import { FullCalendarCoreModule } from 'app/core/core.module';
import { FullCalendarAppRoutingModule } from './app-routing.module';
import { FullCalendarHomeModule } from './home/home.module';
import { FullCalendarEntityModule } from './entities/entity.module';
import { CalendarsModule } from './calendar-list/calendars.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FullCalendarSharedModule,
    FullCalendarCoreModule,
    FullCalendarHomeModule,
    CalendarsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FullCalendarEntityModule,
    FullCalendarAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class FullCalendarAppModule {}
