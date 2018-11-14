app.service('FaresService', FaresService);

FaresService.$inject = [
    '$resource',
    'ConfigurationService'];

function FaresService($resource, configurationService) {
    var service = this;

    service.getFareResource = getFareResource;

    function getFareResource(originCode, destinationCode) {

        var FaresResource = $resource(configurationService.URL_get_fares + '/:origin/:destination', {
            origin: '@origin',
            destination: '@destination'
        }, {
            query: {
                method: 'GET'
            }
        });
        return FaresResource;
    }
}
