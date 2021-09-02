export class ChartModel {
  multi = [];
  view = [];
  showXAxis = false;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = "";
  showYAxisLabel = true;
  yAxisLabel = "";
  timeline = true;
  colorScheme: {
    domain: ["#E91E63", "#CDDC39", "#3F51B5", "#AAAAAA"];
  };
  autoScale = true;
  legendTitle = "";
  yScaleMax=80;
  legend=false;
}
