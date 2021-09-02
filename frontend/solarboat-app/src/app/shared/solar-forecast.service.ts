import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root',
})
export class SolarForecastService {
    constructor(private http: HttpClient) {}

    getCurrentSolarData() {
        return this.http
            .get('https://api.forecast.solar/estimate/watts/47.475498098/19.05333312/0/0/2.1');
    }
}
