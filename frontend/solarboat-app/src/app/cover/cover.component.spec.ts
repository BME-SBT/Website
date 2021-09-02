import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CoverComponent} from './cover.component';
import {ScrollToService} from 'ng2-scroll-to-el';
import {TranslateService} from '@ngx-translate/core';
import {SolarForecastService} from '../shared/solar-forecast.service';
import {SessionStorageService} from '../shared/session-storage.service';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';

class FakeSessionStorageService {
    private jsonData = {
        result: {
            '2020-11-01 06:22:00': 0,
            '2020-11-01 06:34:00': 4,
            '2020-11-01 06:45:00': 38,
            '2020-11-01 07:00:00': 67,
            '2020-11-01 08:00:00': 237
        }
    };
    getItem(key: string) {
        return this.jsonData;
    }

    setItem(key: string, data: Object) {
    }
}
class FakeService {
}

describe('CoverComponent', () => {
    let component: CoverComponent;
    let fixture: ComponentFixture<CoverComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [NoopAnimationsModule],
            providers: [
                CoverComponent,
                {provide: ScrollToService, useClass: FakeService},
                {provide: TranslateService, useClass: FakeService},
                {provide: SessionStorageService, useClass: FakeSessionStorageService},
                {provide: SolarForecastService, useClass: FakeService}
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(CoverComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();

        jasmine.clock().install();
        const baseTime = new Date('November 1, 2020 07:20:18');
        jasmine.clock().mockDate(baseTime);
    });

    afterEach(() => {
        jasmine.clock().uninstall();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('calculate watts from JSON data and date', () => {
        const jsonData = {
            result: {
                '2020-11-01 06:22:00': 0,
                '2020-11-01 06:34:00': 4,
                '2020-11-01 06:45:00': 38,
                '2020-11-01 07:00:00': 67,
                '2020-11-01 08:00:00': 237
            }
        };
        const watts = component.getWattsFromJsonByDate(jsonData, '2020-11-01 07');
        expect(watts).toBe(67);
    });

    it('get date in required format', () => {
        const dateAndHour = component.getCurrentDateAndHour();

        expect(dateAndHour).toBe('2020-11-01 07');
    });

    it('calculate and set watts variable from FakeSessionStorage data', () => {
        component.setWatts();
        expect(component.watts).toBe(67);
    });
});
