import { Motor } from "./motor";
import { Battery } from "./battery";
import { MyCoordinates } from "../model/my-coordinates";

export class BoatData {
  id: Number;
  tilt: MyCoordinates;
  acceleration: MyCoordinates;
  compass: MyCoordinates;
  motor: Motor;
  battery: Battery;
  error: Error;
  extraTemps: Number[] = [];
}
