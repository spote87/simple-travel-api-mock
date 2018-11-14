app.controller('FareDetailsController', FareDetailsController);
FareDetailsController.$inject = [
    'FaresService',
    'ConfigurationService',
    'AirportsService'];

function FareDetailsController(faresService, configurationService, airportsService) {

    var vm = this;
    vm.getFare = getFare;

    init();

    function init() {
        vm.showResultPanel = false;
        vm.airportList = airportsService.getAirports();
        vm.showError = false;
        vm.locations = {};
    }

    function getFare() {
        if (angular.isDefined(vm.origin) && angular.isDefined(vm.destination) && angular.isDefined(vm.origin.code) && angular.isDefined(vm.destination.code)) {
            if (vm.showError) {
                vm.showError = false;
            }
            updateFareData();
        } else {
            vm.showError = true;
            vm.errorMessage = configurationService.LOCATION_NOT_SELECTED_ERROR_MESSAGE;
        }
    }

    function updateFareData() {
        faresService.getFareResource().query({
            origin: vm.origin.code,
            destination: vm.destination.code
        }).$promise.then(function (response) {
            vm.faresData = response;
            vm.showResultPanel = true;
            if (vm.showError) {
                vm.showError = false;
            }
        }).catch(function () {
            vm.errorMessage = configurationService.FARE_DATA_NOT_AVAILABLE_ERROR;
            vm.showError = true;
            vm.showResultPanel = false;
        });
    }

}

