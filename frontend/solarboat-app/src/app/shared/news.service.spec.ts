import { TestBed } from '@angular/core/testing';

import { NewsService } from './news.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Globals} from '../globals';

class MockGlobals {
    BASE_URL = 'solarboatteam.hu';
}

describe('NewsService', () => {
    // We declare the variables that we'll use for the Test Controller and for our Service
    let httpTestingController: HttpTestingController;
    let service: NewsService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [NewsService,
                { provide: Globals, useClass: MockGlobals }],
            imports: [HttpClientTestingModule]
        });

        // We inject our service (which imports the HttpClient) and the Test Controller
        httpTestingController = TestBed.inject(HttpTestingController);
        service = TestBed.inject(NewsService);
    });
    afterEach(() => {
        httpTestingController.verify();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('returned Observable should match the right data', () => {
        const mockNews = {
          title_hu: 'title'
        };

        service.addNews(mockNews)
            .subscribe(res => {
                expect(res.title_hu).toEqual('title');
            });
        const req = httpTestingController.expectOne('solarboatteam.hu/api/news');
        expect(req.request.method).toEqual('POST');
        req.flush(mockNews);
    });

    it('delete function should use the right url', () => {
        service.deleteNews(81);
        const req = httpTestingController.expectOne('solarboatteam.hu/api/news/81');
        expect(req.request.method).toEqual('DELETE');
    });
});
