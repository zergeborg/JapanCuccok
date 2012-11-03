function Slider(_htmlElement, _options) {

    if(!(this instanceof Slider)) {
        //noinspection TailRecursionJS
        return new Slider(_htmlElement, _options);
    }

    var mySelf = this;
    mySelf.eventListener = _htmlElement;
    mySelf.options = _options;
    mySelf.initialState = 'Inactive';
    mySelf.currentState = mySelf.initialState;

    (function () {
        $('#next').click(function (event) {
            var myOwn = mySelf;
            myOwn.handleEvent(event);
        });
    })();

    (function () {
        $('#prev').click(function (event) {
            var myOwn = mySelf;
            myOwn.handleEvent(event);
        });
    })();

}

Slider.prototype = {

    elId                 : undefined,
    ulObject             : undefined,
    params               : [],
    nextTitle            : undefined,
    direction            : 'right', // Which direction?
    lastDirection        : 'right', // The direction in the previous round
    lastCursorX          : 0, // cursor x-position at most recent mouse event
    lastCursorY          : 0, // cursor y-position at most recent mouse event
    width                : 800,
    height               : 600,
    marginLeft           : 0,
    titleOpacity         : 1, // opacity of title and navigation
    titleSpeed           : 1000, // speed of title display
    titleHeight          : 50, // height of the titles
    animationFrequency   : 6000,
    animationDuraion     : 1500,
    indent               : this.marginLeft,
    lastIndent           : this.indent,
    animateProp          : undefined,
    nextImage            : undefined,
    prevImage            : undefined,
    placeHolder          : undefined,
    currentTicker        : undefined, // returned by setInterval, if a ticker is currently running

    initState : function initState() {
        var mySelf = this;
        mySelf.elId = mySelf.eventListener.attr('id');
        mySelf.params[mySelf.elId] = $.extend({}, mySelf.options);
        mySelf.width = mySelf.params[mySelf.elId].width;
        mySelf.height = mySelf.params[mySelf.elId].height;
        mySelf.animationFrequency = 6000;
        mySelf.animationDuraion = 1500;
        return mySelf;
    },

    initParams : function initParams() {
        var mySelf = this;
        mySelf.params[mySelf.elId] = $.extend({}, $.fn.transparentslider.defaults, mySelf.options);
    },

    setDirectionRight : function setDirectionRight() {
        var mySelf = this;
        mySelf.lastDirection = mySelf.direction;
        mySelf.direction = 'right';
    },

    setDirectionLeft : function setDirectionLeft() {
        var mySelf = this;
        mySelf.lastDirection = mySelf.direction;
        mySelf.direction = 'left';
    },

    initPlaceHolder : function initPlaceHolder() {
        var mySelf = this;
        mySelf.placeHolder = $("<li class='ts-li' id='placeholder'><a><span class='imageWrapper'><img/></span></a></li>");
        mySelf.placeHolder.css({'width':this.params[mySelf.elId].width});
//        this.placeHolder.css({'float':'left'});
    },

    createTitleBar: function createTitleBar(i) {
        var mySelf = this;
        // create title bar
        $('#labelWrapper').append("<div class='ts-title' id='ts-title-" + mySelf.elId + i +"'></div>");
        var $title = $('#ts-title-' + mySelf.elId + i);
        return $title;
    },

    initTitleBar: function initTitleBar($title, $titleContent) {
        var mySelf = this;
        $title.css({'height' : mySelf.params[mySelf.elId].titleHeight});
        //$title.css({color: jQuery.Color("rgba(255,255,255,0)")});
        $title.html($titleContent);
        return mySelf;
    },

    initImages : function initImages() {
        var mySelf = this;
        var imgList = $('img', '#slider');
        for(var i = 0, max = imgList.length; i < max; i += 1) {
            var $imageWrapper = $(imgList[i]).parent();
            var $imageWrapperNext = $imageWrapper.next();
            var $title = mySelf.createTitleBar(i);
            var $titleContent = $imageWrapperNext.is('span') ? $imageWrapperNext.html() : '';
            mySelf.initTitleBar($title, $titleContent);
            var $anchor = $imageWrapper.parent();
            var $ts_li = $anchor.parent();
            $ts_li.css({'width':mySelf.width});
            $imageWrapperNext.hide();
        }
        return mySelf;
    },

    //Event listener means the element which is used as the target to attach the content to
    initEventListener : function initEventListener() {
        var mySelf = this;
        var imageWidth = mySelf.width+40; //40px is the background
        var imageHeight = mySelf.height+80; //80px is the background
        // set panel
        $('#' + mySelf.elId).css({
            'clear'   :'both',
            'overflow':'hidden',
            'width'   :imageWidth,
            'height'  :imageHeight,
            //'background-color': 'rgba(255,255,255,1)',
            'background-size': imageWidth+'px '+imageHeight+'px',
            'position':'relative'
        }).wrap("<div class='transparent-slider' id='transparent-slider-" + mySelf.elId + "' />");

        // add slideshow to the DOM tree
        $('.ts-slideshow', '#' + mySelf.elId).attr('id', 'ts-slideshow-' + mySelf.elId);
        mySelf.ulObject = $('#ts-slideshow-' + mySelf.elId);
        return mySelf;
    },

    initSlideshowPanel : function initSlideshowPanel() {
        var mySelf = this;
        var imageWidth = mySelf.width;
        var imageHeight = mySelf.height;
        // slideshow width should be the width of [image_width]*[nr of images]
        mySelf.ulObject.css(
            {
                'width'  :(imageWidth * 3) + (20 * 3), // 20 equals to the left and right margins between images
                'height' :imageHeight-30 //30 pixels are needed to make sure the image fits in its container
            }
        );
        return mySelf;
    },

    setMarginLeft : function setMarginLeft(marginLeft) {
        var mySelf = this;
        mySelf.ulObject.css(
            {
                'margin-left': marginLeft
            }
        );
        return mySelf;
    },

    initMarginLeft : function initMarginLeft() {
        var mySelf = this;
        mySelf.marginLeft = (mySelf.direction === 'left')
            ? ((mySelf.width * 0) - 20)
            : ((mySelf.width * -2) - 30);
    },

    initPrev : function initPrev() {
        $('#prev').appendTo($('#transparent-slider-' + this.elId).children('#slider'));
        $('#prev').css({'position':'absolute', 'float':'left', 'top':'10%', 'left':'0', 'opacity':'0'});
    },

    initNext : function initNext() {
        $('#next').appendTo($('#transparent-slider-' + this.elId).children('#slider'));
        $('#next').css({'position':'absolute', 'float':'right', 'top':'10%', 'right':'0', 'opacity':'0'});
    },

    displaySlideshow : function displaySlideshow() {
        var mySelf = this;
        // Display slideshow
        mySelf.ulObject.show();
        // Display images
        $('#' + mySelf.elId + ' .ts-li').show();
        // Display title
        $(mySelf.nextTitle).show();
    },

    selectNextImage : function selectNextImage() {
        var mySelf = this;
        if(mySelf.direction === 'left') {
            mySelf.nextImage = $('li:first', mySelf.ulObject);
            mySelf.firstImage = mySelf.nextImage.next();
            mySelf.firstImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            mySelf.secondImage = mySelf.nextImage.next().next();
            mySelf.secondImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            mySelf.thirdImage = mySelf.nextImage.next().next().next();
            mySelf.thirdImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            var currentId = mySelf.nextImage.attr("id");
            mySelf.nextTitle = '#ts-title-' + currentId;
        } else {
            mySelf.firstImage = $('li:first', mySelf.ulObject);
            mySelf.firstImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            mySelf.secondImage = $('li:first', mySelf.ulObject).next();
            mySelf.secondImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            mySelf.nextImage = $('li:last', mySelf.ulObject);
            mySelf.nextImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            var currentId = mySelf.nextImage.attr("id");
            mySelf.nextTitle = '#ts-title-' + currentId;
        }
    },

    detachNextImage : function detachNextImage() {
        this.nextImage.remove();
    },

    attachPlaceHolder : function attachPlaceHolder() {
        var mySelf = this;
        if(mySelf.direction === 'left') {
            $('li:last', mySelf.ulObject).after(mySelf.placeHolder);
        } else {
            $('li:first', mySelf.ulObject).before(mySelf.placeHolder);
        }
    },

    replacePlaceHolder : function replacePlaceHolder() {
        // This is needed because jQuery removes the elements after replaceWith called
        var tempImageObject = this.nextImage.clone();
        $('#placeholder').replaceWith(tempImageObject);
    },


    clearCursorPosition : function clearCursorPosition() {
        var mySelf = this;
        mySelf.lastCursorX = -1;
        mySelf.lastCursorY = -1;
    },

    initIndent : function initIndent() {
        var mySelf = this;
        var width = mySelf.width;
        // get the target position for the slideshow div
        if(mySelf.lastDirection === mySelf.direction) {
            mySelf.setMarginLeft(mySelf.marginLeft);
            if(mySelf.direction === 'left') {
                mySelf.indent = mySelf.marginLeft - width;
            } else {
                mySelf.indent = mySelf.marginLeft+width;
            }
        } else {
            mySelf.indent = mySelf.marginLeft;
            mySelf.lastDirection = mySelf.direction;
        }
        mySelf.lastIndent = mySelf.indent;
        return mySelf;
    },

    initAnimateProps : function initAnimateProps() {
        var mySelf = this;
        // jQuery.animate() will use this one!
        mySelf.animateProp = {
            marginLeft :mySelf.indent,
            top        :"0px"
            //paddingLeft:"0px"
        };
        return mySelf;
    },

    animate : function animate() {
        var mySelf = this;
        var titleCB = mySelf.animateTitle;
        var buttonCB = mySelf.animateButtons;
        mySelf.ulObject.stop().animate(
            mySelf.animateProp,
            {
                easing: 'easeOutCubic',
                duration: mySelf.animationDuraion,
                complete: function() {
                    var callbackParam = mySelf;
                    var titleCallback = titleCB;
                    var buttonCallback = buttonCB;
                    titleCallback(callbackParam);
                    buttonCallback(callbackParam);
                }
            }
        );
    },

    animateTitle : function animateTitle(mySelf) {
        $(mySelf.nextTitle).show().animate(
            {
                //color: jQuery.Color("rgba(255,255,255,"+mySelf.titleOpacity+")")
            },
            mySelf.titleSpeed
        );
    },

    animateButtons : function animateButtons(mySelf) {
        $('#next').css({ 'opacity':0 }).animate(
            {
                'opacity':mySelf.titleOpacity
            },
            mySelf.titleSpeed
        );
        $('#prev').css({ 'opacity':0 }).animate(
            {
                'opacity':mySelf.titleOpacity
            },
            mySelf.titleSpeed
        );
    },

    hideTitle : function hideTitle() {
        $('.ts-title').hide();
        //$('.ts-title').css({color: jQuery.Color("rgba(255,255,255,0)")});
    },

    doActionTransition : function doActionTransition(anotherState, anotherEventType, event) {
        var mySelf = this;
        return mySelf.actionTransitionFunctions[anotherState][anotherEventType].call(mySelf, event);
    },

    startTicker : function startTicker() {
        var mySelf = this;
        mySelf.currentTicker = setInterval(function() {
            var myOwn = mySelf;
            // Slide!
            myOwn.doActionTransition('Inactive','timetick',undefined);
        }, mySelf.animationFrequency);

        return mySelf;
    },

    clearTicker : function clearTicker() {
        var ticker = this.currentTicker;
        if (ticker) clearInterval(ticker);
        this.currentTicker = null;
        return this;
    },

    handleEvent: function handleEvent(event) {
        var actionTransitionFunction = this.actionTransitionFunctions[this.currentState][event.type];
        if (!actionTransitionFunction) actionTransitionFunction = this.unexpectedEvent;
        var nextState = actionTransitionFunction.call(this, event);
        if (!nextState) nextState = this.currentState;
        if (!this.actionTransitionFunctions[nextState]) nextState = this.undefinedState(event, nextState);
        this.currentState = nextState;
    },

    unexpectedEvent: function unexpectedEvent(event) {
        var mySelf = this;
        mySelf.cancelTicker();
        alert("TransparentSlider handled unexpected event '" + event.type + "' in state '" + mySelf.currentState + "' for id='" + mySelf.htmlElement.id + "' running browser " + window.navigator.userAgent);
        return mySelf.initialState;
    },

    undefinedState: function undefinedState(event, state) {
        var mySelf = this;
        mySelf.cancelTicker();
        alert("TransparentSlider transitioned to undefined state '" + state + "' from state '" + mySelf.currentState + "' due to event '" + event.type + "' from HTML element id='" + mySelf.htmlElement.id + "' running browser " + window.navigator.userAgent);
        return mySelf.initialState;
    },

    actionTransitionFunctions : {

        Inactive: {
            init: function init(event) {
                var mySelf = this;
                mySelf.initState();
                mySelf.initImages();
                mySelf.initEventListener();
                mySelf.initSlideshowPanel();
                mySelf.initMarginLeft();
                return mySelf.doActionTransition('Inactive', 'run', event);
            },

            run: function run(event) {
                var mySelf = this;
                if(mySelf.lastDirection === mySelf.direction) {
                    mySelf.initMarginLeft();
                    mySelf.selectNextImage();
                }
                mySelf.initPrev();
                mySelf.initNext();
                mySelf.displaySlideshow();
                return mySelf.doActionTransition('Inited', 'move', event);
            },

            timetick: function timetick(event) {
                var mySelf = this;
                return mySelf.doActionTransition('Inactive', 'run', event);
            },

            onnext: function onnext(event) {
                var mySelf = this;
                mySelf.setDirectionRight();
                mySelf.clearTicker();
                return mySelf.doActionTransition('Inactive', 'run', event);
            },

            onprev: function onprev(event) {
                var mySelf = this;
                mySelf.setDirectionLeft();
                mySelf.clearTicker();
                return mySelf.doActionTransition('Inactive', 'run', event);
            },

            click: function click(event) {
                var mySelf = this;
                var $eventTarget = $(event.target);
                if($eventTarget.parent().attr('id') === 'next' || $eventTarget.attr('id') === 'next') {
                    return mySelf.doActionTransition('Inactive', 'onnext', event);
                } else {
                    return mySelf.doActionTransition('Inactive', 'onprev', event);
                }
            }

        },

        Inited: {
            move: function move(event) {
                var mySelf = this;
                if(mySelf.lastDirection === mySelf.direction) {
                    mySelf.initPlaceHolder();
                    mySelf.attachPlaceHolder();
                    mySelf.replacePlaceHolder();
                }
                return mySelf.doActionTransition('Move', 'slide', event);
            },

            onnext: function onnext(event) {
                var mySelf = this;
                return mySelf.doActionTransition('Inactive', 'onnext', event);
            },

            onprev: function onprev(event) {
                var mySelf = this;
                return mySelf.doActionTransition('Inactive', 'onprev', event);
            },

            click: function click(event) {
                var mySelf = this;
                var $eventTarget = $(event.target);
                if($eventTarget.attr('id') === 'next') {
                    return mySelf.doActionTransition('Inited', 'onnext', event);
                } else {
                    return mySelf.doActionTransition('Inited', 'onPrev', event);
                }
            }

        },

        Move: {
            slide: function slide(event) {
                var mySelf = this;
                mySelf.initIndent();
                mySelf.initAnimateProps();
                mySelf.animate();
                return mySelf.doActionTransition('Finish', 'reset', event);
            },

            onnext: function onnext(event) {
                var mySelf = this;
                mySelf.setDirectionRight();
                return mySelf.doActionTransition('Interrupt', 'slide', event);
            },

            onprev: function onprev(event) {
                var mySelf = this;
                mySelf.setDirectionLeft();
                return mySelf.doActionTransition('Interrupt', 'slide', event);
            },

            click: function click(event) {
                var mySelf = this;
                var $eventTarget = $(event.target);
                if($eventTarget.attr('id') === 'next') {
                    return mySelf.doActionTransition('Move', 'onnext', event);
                } else {
                    return mySelf.doActionTransition('Move', 'onPrev', event);
                }
            }

        },

        Interrupt: {
            slide: function slide() {
                var mySelf = this;
                return mySelf.doActionTransition('Move', 'slide', event);
            }
        },

        Finish: {
            reset: function reset(event) {
                var mySelf = this;
                mySelf.hideTitle();
                mySelf.detachNextImage();
                mySelf.clearCursorPosition();
                mySelf.clearTicker();
                mySelf.startTicker();
                return "Inactive";
            }
        }

    }

};
