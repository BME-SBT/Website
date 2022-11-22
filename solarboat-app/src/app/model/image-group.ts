import { Image } from "./image";
import { Video } from "./video";
export class ImageGroup {
    id: number;
    name_hu: string;
    name_en: string;
    images: Image[];
    videos: Video[];
    coverImageId: number;
}