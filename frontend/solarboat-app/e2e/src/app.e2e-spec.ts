import { AppPage } from './app.po';
import { browser, logging } from 'protractor';

describe('BME Solar Boat Team App e2e tests', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display welcome message', () => {
    page.navigateToNews();
    const title = page.getTitleText();
    expect(title).toEqual('HÍREK');
  });

  it('should sign in as admin and add one news', () => {
    page.navigateToLogin();
    page.setUsernameField('sb-admin');
    page.setPasswordField('uszikAhajo!');
    browser.sleep(1000);
    page.clickLoginBtn();
    browser.sleep(1000);
    page.navigateToNews();
    const title = page.getFormTitleText();
    expect(title).toEqual('ÚJ HÍR HOZZÁADÁSA');
    page.setHuNewsTitle('Teszt cím');
    page.setHuNewsText('Teszt tartalom teszt tartalom teszt tartalom teszt tartalom teszt tartalom teszt tartalom');
    browser.sleep(1000);
    page.selectEnSettings();
    page.setEnNewsTitle('Test title');
    page.setEnNewsText('Test content test content test content test content test content test content test content');
    page.clickOnSubmit();
    browser.sleep(4000);
    const newsTitle = page.getFirstNewsTitle();
    expect(newsTitle).toEqual('TESZT CÍM');
    browser.sleep(4000);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
