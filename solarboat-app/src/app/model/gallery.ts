import { Image } from "./image";
import { Video } from "./video";

export class Gallery{
    id: number;
    name_hu: string;
    name_en: string;
    coverImage: Image;
    images: Image[];
    videos: Video[];
    date: string;
}