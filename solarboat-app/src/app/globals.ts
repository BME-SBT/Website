import { Injectable } from '@angular/core';

@Injectable()
export class Globals {
        BASE_URL = 'http://localhost:8080';
       // BASE_URL = 'https://solarboatteam.hu';

        formatDate(date) {
                const newDate = new Date(date);
                const mm = newDate.getMonth() + 1; // getMonth() is zero-based
                const dd = newDate.getDate();

                const resultDate = [newDate.getFullYear(),
                        (mm > 9 ? '' : '0') + mm,
                        (dd > 9 ? '' : '0') + dd
                ].join('-');

                return resultDate;
        }
}
