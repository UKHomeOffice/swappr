(function () {
    'use strict';

    var el = document.querySelector('.js-menu');

    function toggleClass() {
        var pattern = /(?:^|\s)is-active(?!\S)/g,
            isActive = pattern.test(el.className);

        if (isActive) {
            el.className = el.className.replace(pattern, '');
        } else {
            el.className += ' is-active';
        }
    }

    if (el.addEventListener) {
        el.addEventListener('click', toggleClass, false);
    } else if (el.attachEvent) {
        el.attachEvent('onclick', toggleClass);
    }

}());