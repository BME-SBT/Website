import {Member} from './member';

export class Team {
    id: number;
    teamType: number;
    name_hu: string;
    description_hu: string;
    name_en: string;
    description_en: string;
    leader: Member;
    members: Member[];
}
