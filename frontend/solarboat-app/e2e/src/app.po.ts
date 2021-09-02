import { browser, by, element } from 'protractor';

export class AppPage {
  navigateToNews(): Promise<unknown> {
    return browser.get('http://localhost:4200/#/news') as Promise<unknown>;
  }
  navigateToLogin(): Promise<unknown> {
    return browser.get('http://localhost:4200/#/admin') as Promise<unknown>;
  }
  getTitleText(): Promise<string> {
    return element(by.css('h1')).getText() as Promise<string>;
  }

  setUsernameField(text: string) {
    element(by.id('username')).sendKeys(text);
  }

  setPasswordField(text: string) {
    element(by.id('password')).sendKeys(text);
  }

  clickLoginBtn() {
    element(by.id('login-btn')).click();
  }

  getFormTitleText(): Promise<string> {
    return element(by.id('form-title')).getText() as Promise<string>;
  }

  setHuNewsTitle(text: string) {
    element(by.id('title')).sendKeys(text);
  }

  setHuNewsText(text: string) {
    element(by.id('content')).sendKeys(text);
  }

  setEnNewsTitle(text: string) {
    element(by.id('title_en')).sendKeys(text);
  }

  setEnNewsText(text: string) {
    element(by.id('content_en')).sendKeys(text);
  }

  selectEnSettings() {
    element(by.id('english-tab')).click();
  }

  clickOnSubmit() {
    element(by.id('submit-btn')).click();
  }

  getFirstNewsTitle(): Promise<string> {
    return element(by.id('news-title')).getText() as Promise<string>;
  }
}
