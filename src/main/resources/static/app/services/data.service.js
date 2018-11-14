app.service('DataService', DataService);


function DataService() {
    var service = this;

    var fareData;

    service.getFareData = getFareData;
    service.setFareData = setFareData;

    function getFareData() {
        return fareData || [];
    }

    function setFareData(data) {
        fareData = data;
    }

}
