import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class SessionStorageService {
    constructor() {}

    getItem(key: string) {
        const storageData = sessionStorage.getItem(key);
        if (storageData) {
            return  JSON.parse(storageData);
        }
        return null;
    }

    // tslint:disable-next-line:ban-types
    setItem(key: string, data: Object) {
        sessionStorage.setItem(key, JSON.stringify(data));
    }
}
