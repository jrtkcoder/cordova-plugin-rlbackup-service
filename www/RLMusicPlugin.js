var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'RLMusicPlugin', 'coolMethod', [arg0]);
};

exports.startMusicService = function (title, content, para) {
    exec(null, null, 'RLMusicPlugin', 'startMusicService', [title, content, para]);
};

exports.setNotification = function (title, content, para) {
    exec(null, null, 'RLMusicPlugin', 'setNotification', [title, content, para]);
};
