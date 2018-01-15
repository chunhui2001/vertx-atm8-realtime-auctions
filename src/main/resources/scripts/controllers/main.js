angular.module('bidding').controller('main', ['$scope', '$timeout', '$http', function ($scope, $timeout, $http) {

    $scope.auctionId = 1;
    $scope.bidStep = 1.00;
    $scope.feeds = [];

    $scope.setMessage = function (message) {
        $scope.message = message;

        $timeout(function () {
            $scope.message = {};
        }, 3000);
    };

    var requestParams = {
        method: 'GET',
        url: 'http://192.168.189.176:8080/api/auctions/' + $scope.auctionId,
        headers: {
            'Accept': 'application/json'
        }
    };

    $http(requestParams).then(
        function (responseData) {
            $scope.endingTime = responseData.data.endingTime;
            $scope.currentPrice = responseData.data.price;
        },
        function (error) {
            throw new Error('Failed to load current auction price: ' + error.statusText);
        }
    );

    var eventBus = new EventBus('http://192.168.189.176:8082/eventbus');

    eventBus.onopen = function () {
        eventBus.registerHandler('auction.' + $scope.auctionId, function (err , message) {

            var message = JSON.parse(message.body);

            angular.extend(message, {time: new Date()});
            $scope.currentPrice = message.price;
            $scope.feeds.push(message);
            $scope.setMessage({status: 'success', text: "Oferta zmieniła swoją cenę"});
        });
    }

}]);
