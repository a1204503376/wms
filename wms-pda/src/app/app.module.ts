import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ErrorHandler, NgModule,NO_ERRORS_SCHEMA } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { HttpClientModule } from "@angular/common/http";
import { Toast } from '@ionic-native/toast';
import { MyApp } from './app.component';
import { HttpModule, JsonpModule } from '@angular/http';
import { IonicStorageModule } from '@ionic/storage';
import { AppService } from "../services/App.service";
import { NativeService } from "../services/NativeService";
import { CommonService } from "../utils/common";
import { BarcodeService } from '../services/BarCodeService';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AndroidPermissions } from '@ionic-native/android-permissions';
import { File } from '@ionic-native/file';
import { FileTransfer } from '@ionic-native/file-transfer';
import { AppVersion } from '@ionic-native/app-version';
import { FileOpener } from '@ionic-native/file-opener';
import { Printer } from '@ionic-native/printer';
import { ComponentsModule } from '../components/components.module';
import { Keyboard } from '@ionic-native/keyboard';
import { DirectivesModule } from "../directives/directives.module";
import { CommonModule } from '@angular/common';

//公共指令
const serviceList = [
  AppService,
  NativeService,
  CommonService,
  BarcodeService,
];


@NgModule({
  declarations: [
    MyApp,
  ],
  imports: [
    DirectivesModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    IonicModule.forRoot(MyApp, { conMode: 'md', mode: 'ios', backButtonText: '' }),
    ComponentsModule,
    HttpClientModule,
    HttpModule,
    JsonpModule,
    IonicStorageModule.forRoot()
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp
  ],
  providers: [
    Keyboard,
    AndroidPermissions,
    File,
    FileTransfer,
    AppVersion,
    FileOpener,
    serviceList, 
    StatusBar,
    SplashScreen,
    Toast,
    Printer,
    { provide: ErrorHandler, useClass: IonicErrorHandler }
  ]
})
export class AppModule { }
