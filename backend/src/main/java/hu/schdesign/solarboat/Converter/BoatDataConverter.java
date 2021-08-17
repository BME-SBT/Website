package hu.schdesign.solarboat.Converter;

import hu.schdesign.solarboat.model.Boat.Coordinates;
import hu.schdesign.solarboat.model.Boat.DataAnalysis;
import hu.schdesign.solarboat.model.BoatData;
import hu.schdesign.solarboat.model.DataGroup;
import hu.schdesign.solarboat.model.ResponseBoatData;
import hu.schdesign.solarboat.model.DataPair;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BoatDataConverter {
    public ResponseBoatData convertBoatDataToResponseBoatData(BoatData bd) {
        ResponseBoatData response = new ResponseBoatData();

        ArrayList<ArrayList<DataPair<String, Double>>> tiltTemp = new ArrayList<>();
        ArrayList<ArrayList<DataPair<String, Double>>> accelerationTemp = new ArrayList<>();
        ArrayList<Coordinates> compassTemp = new ArrayList<>();
        ArrayList<DataPair<String, Double>> distanceTemp = new ArrayList<>();
        ArrayList<Double> velocityTemp = new ArrayList<>();
        ArrayList<ArrayList<DataPair<String, Double>>> batteryTemp = new ArrayList<>();
        ArrayList<ArrayList<DataPair<String, Double>>> motorTemp = new ArrayList<>();


        ArrayList<DataPair<String, Double>> tiltX = new ArrayList<>();
        ArrayList<DataPair<String, Double>> tiltY = new ArrayList<>();
        ArrayList<DataPair<String, Double>> tiltZ = new ArrayList<>();
        ArrayList<DataPair<String, Double>> accelerationX = new ArrayList<>();
        ArrayList<DataPair<String, Double>> accelerationY = new ArrayList<>();
        ArrayList<DataPair<String, Double>> accelerationZ = new ArrayList<>();
        ArrayList<DataPair<String, Double>> compassX = new ArrayList<>();
        ArrayList<DataPair<String, Double>> compassY = new ArrayList<>();
        ArrayList<DataPair<String, Double>> compassZ = new ArrayList<>();
        ArrayList<DataPair<String, Double>> in = new ArrayList<>();
        ArrayList<DataPair<String, Double>> out = new ArrayList<>();
        ArrayList<DataPair<String, Double>> SoC = new ArrayList<>();
        ArrayList<DataPair<String, Double>> temp = new ArrayList<>();
        ArrayList<DataPair<String, Double>> RmP = new ArrayList<>();
        ArrayList<DataPair<String, Double>> mTemp = new ArrayList<>();

        response.setId(bd.getDate());

        tiltX.add(new DataPair<String, Double>(response.getId(), bd.getTilt().getX()));
        tiltY.add(new DataPair<String, Double>(response.getId(), bd.getTilt().getY()));
        tiltZ.add(new DataPair<String, Double>(response.getId(), bd.getTilt().getZ()));


        accelerationX.add(new DataPair<String, Double>(response.getId(), bd.getAcceleration().getX()));
        accelerationY.add(new DataPair<String, Double>(response.getId(), bd.getAcceleration().getY()));
        accelerationZ.add(new DataPair<String, Double>(response.getId(), bd.getAcceleration().getZ()));


//        compassX.add(new dataPair<String, Integer>(response.getId(), bd.getCompass().getX()));
//        compassY.add(new dataPair<String, Integer>(response.getId(), bd.getCompass().getY()));
//        compassZ.add(new dataPair<String, Integer>(response.getId(), bd.getCompass().getZ()));


        in.add(new DataPair<String, Double>(response.getId(), bd.getBattery().get_in()));
        out.add(new DataPair<String, Double>(response.getId(), bd.getBattery().get_out()));
        SoC.add(new DataPair<String, Double>(response.getId(), bd.getBattery().getSoC()));
        temp.add(new DataPair<String, Double>(response.getId(), bd.getBattery().getTemp()));


        RmP.add(new DataPair<String, Double>(response.getId(), bd.getMotor().getRpM()));
        mTemp.add(new DataPair<String, Double>(response.getId(), bd.getMotor().getTemp()));

        if (bd.getError() != null) {
            response.getErrors().add(bd.getError());
        }

        tiltTemp.add(tiltX);
        tiltTemp.add(tiltY);
        tiltTemp.add(tiltZ);
        response.setTilt(tiltTemp);

        accelerationTemp.add(accelerationX);
        accelerationTemp.add(accelerationY);
        accelerationTemp.add(accelerationZ);
        response.setAcceleration(accelerationTemp);

        compassTemp.add(bd.getCompass());
//        compassTemp.add(compassY);
//        compassTemp.add(compassZ);
        response.setCompass(compassTemp);

        velocityTemp.add(bd.getVelocity());
        response.setVelocity(velocityTemp);
        distanceTemp.add(new DataPair<String, Double>(response.getId(), bd.getSumDistance()));
        response.setDistance(distanceTemp);

        batteryTemp.add(in);
        batteryTemp.add(out);
        batteryTemp.add(SoC);
        batteryTemp.add(temp);
        response.setBattery(batteryTemp);

        motorTemp.add(RmP);
        motorTemp.add(mTemp);
        response.setMotor(motorTemp);

        return response;
    }

    public ArrayList<DataPair<String, Double>> coordinatesToList(Coordinates coordinates, String date) {
        ArrayList<DataPair<String, Double>> resultList = new ArrayList<>();
        resultList.add(new DataPair<String, Double>(date, coordinates.getX()));
        resultList.add(new DataPair<String, Double>(date, coordinates.getY()));
        resultList.add(new DataPair<String, Double>(date, coordinates.getZ()));
        return resultList;
    }

    public ResponseBoatData convertDataGroupToResponseBoatData(DataGroup dataGroup) {
        ResponseBoatData response = new ResponseBoatData();
        ArrayList<ArrayList<DataPair<String, Double>>> tiltTemp = new ArrayList<>();
        ArrayList<ArrayList<DataPair<String, Double>>> accelerationTemp = new ArrayList<>();
        ArrayList<DataPair<String, Double>> distanceTemp = new ArrayList<>();
        ArrayList<Coordinates> compassTemp = new ArrayList<>();
        ArrayList<Double> velocityTemp = new ArrayList<>();
        ArrayList<ArrayList<DataPair<String, Double>>> batteryTemp = new ArrayList<>();
        ArrayList<ArrayList<DataPair<String, Double>>> motorTemp = new ArrayList<>();


        ArrayList<DataPair<String, Double>> x = new ArrayList<>();
        ArrayList<DataPair<String, Double>> y = new ArrayList<>();
        ArrayList<DataPair<String, Double>> z = new ArrayList<>();
        ArrayList<DataPair<String, Double>> ax = new ArrayList<>();
        ArrayList<DataPair<String, Double>> ay = new ArrayList<>();
        ArrayList<DataPair<String, Double>> az = new ArrayList<>();
//        ArrayList<dataPair<String, Integer>> cx = new ArrayList<>();
//        ArrayList<dataPair<String, Integer>> cy = new ArrayList<>();
//        ArrayList<dataPair<String, Integer>> cz = new ArrayList<>();
        ArrayList<DataPair<String, Double>> in = new ArrayList<>();
        ArrayList<DataPair<String, Double>> out = new ArrayList<>();
        ArrayList<DataPair<String, Double>> SoC = new ArrayList<>();
        ArrayList<DataPair<String, Double>> temp = new ArrayList<>();
        ArrayList<DataPair<String, Double>> RmP = new ArrayList<>();
        ArrayList<DataPair<String, Double>> mTemp = new ArrayList<>();

        ArrayList<Double> analysis_tiltX = new ArrayList<>();
        ArrayList<Double> analysis_tiltY = new ArrayList<>();
        ArrayList<Double> analysis_tiltZ = new ArrayList<>();
        ArrayList<Double> analysis_accelerationX = new ArrayList<>();
        ArrayList<Double> analysis_accelerationY = new ArrayList<>();
        ArrayList<Double> analysis_accelerationZ = new ArrayList<>();
        ArrayList<Double> analysis_compassX = new ArrayList<>();
        ArrayList<Double> analysis_compassY = new ArrayList<>();
        ArrayList<Double> analysis_compassZ = new ArrayList<>();
        ArrayList<Double> analysis_in = new ArrayList<>();
        ArrayList<Double> analysis_out = new ArrayList<>();
        ArrayList<Double> analysis_SoC = new ArrayList<>();
        ArrayList<Double> analysis_temp = new ArrayList<>();
        ArrayList<Double> analysis_RmP = new ArrayList<>();
        ArrayList<Double> analysis_mTemp = new ArrayList<>();

        for (BoatData bd : dataGroup.getBoatDataList()) {
            response.setId(bd.getDate());
            x.add(new DataPair<String, Double>(response.getId(), bd.getTilt().getX()));
            y.add(new DataPair<String, Double>(response.getId(), bd.getTilt().getY()));
            z.add(new DataPair<String, Double>(response.getId(), bd.getTilt().getZ()));
            analysis_tiltX.add(bd.getTilt().getX());
            analysis_tiltY.add(bd.getTilt().getY());
            analysis_tiltZ.add(bd.getTilt().getZ());

            ax.add(new DataPair<String, Double>(response.getId(), bd.getAcceleration().getX()));
            ay.add(new DataPair<String, Double>(response.getId(), bd.getAcceleration().getY()));
            az.add(new DataPair<String, Double>(response.getId(), bd.getAcceleration().getZ()));
            analysis_accelerationX.add(bd.getAcceleration().getX());
            analysis_accelerationY.add(bd.getAcceleration().getY());
            analysis_accelerationZ.add(bd.getAcceleration().getZ());

            compassTemp.add(bd.getCompass());
            velocityTemp.add(bd.getVelocity());
//            cx.add(new dataPair<String, Integer>(response.getId(), bd.getCompass().getX()));
//            cy.add(new dataPair<String, Integer>(response.getId(), bd.getCompass().getY()));
//            cz.add(new dataPair<String, Integer>(response.getId(), bd.getCompass().getZ()));
            analysis_compassX.add(bd.getCompass().getX());
            analysis_compassY.add(bd.getCompass().getY());
            analysis_compassZ.add(bd.getCompass().getZ());

            in.add(new DataPair<String, Double>(response.getId(), bd.getBattery().get_in()));
            out.add(new DataPair<String, Double>(response.getId(), bd.getBattery().get_out()));
            SoC.add(new DataPair<String, Double>(response.getId(), bd.getBattery().getSoC())); //ezt majd nem így kell!
            temp.add(new DataPair<String, Double >(response.getId(), bd.getBattery().getTemp())); //ezt sem!!
            analysis_in.add(bd.getBattery().get_in());
            analysis_out.add(bd.getBattery().get_out());
            analysis_SoC.add(bd.getBattery().getSoC()); //ezt majd nem így kell!
            analysis_temp.add(bd.getBattery().getTemp()); //ezt sem!!

            RmP.add(new DataPair<String, Double>(response.getId(), bd.getMotor().getRpM()));
            mTemp.add(new DataPair<String, Double>(response.getId(), bd.getMotor().getTemp()));
            analysis_RmP.add(bd.getMotor().getRpM());
            analysis_mTemp.add(bd.getMotor().getTemp());
            if (bd.getError() != null) {
                response.getErrors().add(bd.getError());
            }
            distanceTemp.add(new DataPair<>(response.getId(), bd.getSumDistance()));
        }
        tiltTemp.add(x);
        tiltTemp.add(y);
        tiltTemp.add(z);
        response.setTilt(tiltTemp);

        accelerationTemp.add(ax);
        accelerationTemp.add(ay);
        accelerationTemp.add(az);
        response.setAcceleration(accelerationTemp);

//        compassTemp.add(bd.get);
//        compassTemp.add(cy);
//        compassTemp.add(cz);
        response.setCompass(compassTemp);
        response.setVelocity(velocityTemp);
        response.setDistance(distanceTemp);

        batteryTemp.add(in);
        batteryTemp.add(out);
        batteryTemp.add(SoC);
        batteryTemp.add(temp);
        response.setBattery(batteryTemp);

        motorTemp.add(RmP);
        motorTemp.add(mTemp);
        response.setMotor(motorTemp);

        DataAnalysis analAccelerationX = new DataAnalysis();
        analAccelerationX.setMax(analysis_accelerationX.stream().max(Double::compareTo).orElse(null));
        analAccelerationX.setMin(analysis_accelerationX.stream().min(Double::compareTo).orElse(null));
        analAccelerationX.setAverage(analysis_accelerationX.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analAccelerationX.setName("X");
        DataAnalysis analAccelerationY = new DataAnalysis();
        analAccelerationY.setMax(analysis_accelerationY.stream().max(Double::compareTo).orElse(null));
        analAccelerationY.setMin(analysis_accelerationY.stream().min(Double::compareTo).orElse(null));
        analAccelerationY.setAverage(analysis_accelerationY.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analAccelerationY.setName("Y");
        DataAnalysis analAccelerationZ = new DataAnalysis();
        analAccelerationZ.setMax(analysis_accelerationZ.stream().max(Double::compareTo).orElse(null));
        analAccelerationZ.setMin(analysis_accelerationZ.stream().min(Double::compareTo).orElse(null));
        analAccelerationZ.setAverage(analysis_accelerationZ.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analAccelerationZ.setName("Z");
        response.setAccelerationAnalysis(new ArrayList<>());
        response.getAccelerationAnalysis().add(analAccelerationX);
        response.getAccelerationAnalysis().add(analAccelerationY);
        response.getAccelerationAnalysis().add(analAccelerationZ);

        DataAnalysis analTiltX = new DataAnalysis();
        analTiltX.setMax(analysis_tiltX.stream().max(Double::compareTo).orElse(null));
        analTiltX.setMin(analysis_tiltX.stream().min(Double::compareTo).orElse(null));
        analTiltX.setAverage(analysis_tiltX.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analTiltX.setName("X");
        DataAnalysis analTiltY = new DataAnalysis();
        analTiltY.setMax(analysis_tiltY.stream().max(Double::compareTo).orElse(null));
        analTiltY.setMin(analysis_tiltY.stream().min(Double::compareTo).orElse(null));
        analTiltY.setAverage(analysis_tiltY.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analTiltY.setName("Y");
        DataAnalysis analTiltZ = new DataAnalysis();
        analTiltZ.setMax(analysis_tiltZ.stream().max(Double::compareTo).orElse(null));
        analTiltZ.setMin(analysis_tiltZ.stream().min(Double::compareTo).orElse(null));
        analTiltZ.setAverage(analysis_tiltZ.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analTiltZ.setName("Z");
        response.setTiltAnalysis(new ArrayList<>());
        response.getTiltAnalysis().add(analTiltX);
        response.getTiltAnalysis().add(analTiltY);
        response.getTiltAnalysis().add(analTiltZ);

        DataAnalysis analCompassX = new DataAnalysis();
        analCompassX.setMax(analysis_compassX.stream().max(Double::compareTo).orElse(null));
        analCompassX.setMin(analysis_compassX.stream().min(Double::compareTo).orElse(null));
        analCompassX.setAverage(analysis_compassX.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analCompassX.setName("X");
        DataAnalysis analCompassY = new DataAnalysis();
        analCompassY.setMax(analysis_compassY.stream().max(Double::compareTo).orElse(null));
        analCompassY.setMin(analysis_compassY.stream().min(Double::compareTo).orElse(null));
        analCompassY.setAverage(analysis_compassY.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analCompassY.setName("Y");
        DataAnalysis analCompassZ = new DataAnalysis();
        analCompassZ.setMax(analysis_compassZ.stream().max(Double::compareTo).orElse(null));
        analCompassZ.setMin(analysis_compassZ.stream().min(Double::compareTo).orElse(null));
        analCompassZ.setAverage(analysis_compassZ.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analCompassZ.setName("Z");
        response.setCompassAnalysis(new ArrayList<>());
        response.getCompassAnalysis().add(analCompassX);
        response.getCompassAnalysis().add(analCompassY);
        response.getCompassAnalysis().add(analCompassZ);

        DataAnalysis analBatteryIn = new DataAnalysis();
        analBatteryIn.setMax(analysis_in.stream().max(Double::compareTo).orElse(null));
        analBatteryIn.setMin(analysis_in.stream().min(Double::compareTo).orElse(null));
        analBatteryIn.setAverage(analysis_in.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analBatteryIn.setName("Battery in");
        DataAnalysis analBatteryOut = new DataAnalysis();
        analBatteryOut.setMax(analysis_out.stream().max(Double::compareTo).orElse(null));
        analBatteryOut.setMin(analysis_out.stream().min(Double::compareTo).orElse(null));
        analBatteryOut.setAverage(analysis_out.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analBatteryOut.setName("Battery out");
        DataAnalysis analBatterySoC = new DataAnalysis();
        analBatterySoC.setMax(analysis_SoC.stream().max(Double::compareTo).orElse(null));
        analBatterySoC.setMin(analysis_SoC.stream().min(Double::compareTo).orElse(null));
        analBatterySoC.setAverage(analysis_SoC.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analBatterySoC.setName("Battery SoC");
        DataAnalysis analBatteryTemp = new DataAnalysis();
        analBatteryTemp.setMax(analysis_temp.stream().max(Double::compareTo).orElse(null));
        analBatteryTemp.setMin(analysis_temp.stream().min(Double::compareTo).orElse(null));
        analBatteryTemp.setAverage(analysis_temp.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analBatteryTemp.setName("Battery Temperature");
        response.setBatteryAnalysis(new ArrayList<>());
        response.getBatteryAnalysis().add(analBatteryIn);
        response.getBatteryAnalysis().add(analBatteryOut);
        response.getBatteryAnalysis().add(analBatterySoC);
        response.getBatteryAnalysis().add(analBatteryTemp);

        DataAnalysis analMotorRmP = new DataAnalysis();
        analMotorRmP.setMax(analysis_RmP.stream().max(Double::compareTo).orElse(null));
        analMotorRmP.setMin(analysis_RmP.stream().min(Double::compareTo).orElse(null));
        analMotorRmP.setAverage(analysis_RmP.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analMotorRmP.setName("Motor RmP");
        DataAnalysis analMotorTemp = new DataAnalysis();
        analMotorTemp.setMax(analysis_mTemp.stream().max(Double::compareTo).orElse(null));
        analMotorTemp.setMin(analysis_mTemp.stream().min(Double::compareTo).orElse(null));
        analMotorTemp.setAverage(analysis_mTemp.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
        analMotorTemp.setName("Motor SoC");

        response.setMotorAnalysis(new ArrayList<>());
        response.getMotorAnalysis().add(analMotorRmP);
        response.getMotorAnalysis().add(analMotorTemp);

        return response;
    }

}
