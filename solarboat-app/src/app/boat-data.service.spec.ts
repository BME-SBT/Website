import { TestBed } from '@angular/core/testing';

import { BoatDataService } from './boat-data.service';

describe('BoatDataService', () => {
  let service: BoatDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BoatDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
