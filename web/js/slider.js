function Slider(_htmlElement, _options) {

    if(!(this instanceof Slider)) {
        //noinspection TailRecursionJS
        return new Slider(_htmlElement, _options);
    }

    this.eventListener = _htmlElement;
    this.options = _options;
    this.initialState = 'Inactive';
    this.currentState = this.initialState;

    var mySelf = this;

    (function () {
        $('#next').click(function (event) {
            mySelf.handleEvent(event);
        });
    })();

    (function () {
        $('#prev').click(function (event) {
            mySelf.handleEvent(event);
        });
    })();

}

Slider.prototype = {

    mySelf               : undefined,
    elId                 : undefined,
    params               : [],
    images               : [],
    links                : [],
    linksTarget          : [],
    titles               : [],
    nextTitle            : undefined,
    lastTitle            : undefined,
    lastImageTriplet     : [], // image list at most recent slide event
    direction            : 'right', // Which direction?
    lastDirection        : 'right', // The direction in the previous round
    lastCursorX          : 0, // cursor x-position at most recent mouse event
    lastCursorY          : 0, // cursor y-position at most recent mouse event
    width                : 800,
    height               : 600,
    marginLeft           : 0,
    conf                 : undefined,
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
        this.mySelf = this;
        this.elId = this.eventListener.attr('id');
        this.params[this.elId] = $.extend({}, this.options);
        this.width = this.params[this.elId].width;
        this.height = this.params[this.elId].height;
        this.images[this.elId] = [];
        this.links[this.elId] = [];
        this.linksTarget[this.elId] = [];
        this.titles[this.elId] = [];
        this.animationFrequency = 6000;
        this.animationDuraion = 1500;
        return this;
    },

    initParams : function initParams() {
        this.params[this.elId] = $.extend({}, $.fn.transparentslider.defaults, this.options);
    },

    setDirectionRight : function setDirectionRight() {
        this.lastDirection = this.direction;
        this.direction = 'right';
    },

    setDirectionLeft : function setDirectionLeft() {
        this.lastDirection = this.direction;
        this.direction = 'left';
    },

    initPlaceHolder : function initPlaceHolder() {
        this.placeHolder = $("<li class='ts-li' id='placeholder'><a><span class='imageWrapper'><img/></span></a></li>");
        this.placeHolder.css({'width':this.params[this.elId].width});
//        this.placeHolder.css({'float':'left'});
    },

    initTitleBar: function initTitleBar(i) {
        // create title bar
        $('#labelWrapper').append("<div class='ts-title' id='ts-title-" + this.elId + i +"'></div>");
        var $title = $('#ts-title-' + this.elId + i);
        $title.css({'display': 'none'});
        $title.css({'position': 'relative'});
        $title.css({'bottom':'0'});
        $title.css({'left': '0'});
        $title.css({'z-index': '1000'});
        $title.css({'height' : this.params[this.elId].titleHeight});
        //$title.css({color: jQuery.Color("rgba(255,255,255,0)")});
        $title.html(this.titles[this.elId][i]);
        return this;
    },

    initImages : function initImages() {
        // create images, links and titles arrays
        var imgList = $('#slider img');
        for(var i = 0; i < imgList.length; i += 1) {
            var $imageWrapper = $(imgList[i]).parent();
            var $imageWrapperNext = $imageWrapper.next();
            this.images[this.elId][i] = $(imgList[i]).attr('src');
            this.links[this.elId][i] = $imageWrapper.is('a') ? $imageWrapper.attr('href') : '';
            this.linksTarget[this.elId][i] = $imageWrapper.is('a') ? $imageWrapper.attr('target') : '';
            this.titles[this.elId][i] = $imageWrapperNext.is('span') ? $imageWrapperNext.html() : '';
            var $anchor = $imageWrapper.parent();
            $anchor.wrap("<li class='ts-li'/>");
            var $ts_li = $anchor.parent();
            $ts_li.attr('id', this.elId + i);
            $ts_li.css({'width':this.width});
            $ts_li.css({
                'display'      : 'inline',
                'float'        : 'left',
                'margin-right' : 10, //TODO This value should be a parameter
                'margin-left' : 10 //TODO This value should be a parameter
            });
            this.initTitleBar(i);
            $imageWrapperNext.hide();
        }
        return this;
    },

    initEventListener : function initEventListener() {
        var imageWidth = this.width+40; //40px is the background
        var imageHeight = this.height+80; //80px is the background
        var numberOfImages = this.images[this.elId].length + 1; // +1 because of the placeholder
        // set panel
        $('#' + this.elId).css({
            'clear'   :'both',
            'overflow':'hidden',
            'width'   :imageWidth,
            'height'  :imageHeight,
            //'background-color': 'rgba(255,255,255,1)',
            'background-size': imageWidth+'px '+imageHeight+'px',
            'position':'relative'
        }).wrap("<div class='transparent-slider' id='transparent-slider-" + this.elId + "' />");

        // add slideshow to the DOM tree
        $('#' + this.elId + ' .ts-li').wrapAll("<ul class='ts-slideshow' id='ts-slideshow-" + this.elId + "' />");
        return this;
    },

    initSlideshowPanel : function initSlideshowPanel() {
        var imageWidth = this.width;
        var imageHeight = this.height;
        var numberOfImages = this.images[this.elId].length + 1; // +1 because of the placeholder
        // slideshow width should be the width of [image_width]*[nr of images]
        $('#ts-slideshow-' + this.elId).css(
            {
                'width'  :(imageWidth * 3) + (20 * 3), // 10 equals to the left and right margins between images
                'height' :imageHeight-30, //30 pixels are needed to make sure the image fits in its container
                'padding-left': 20,
                'display':''
            }
        );
        return this;
    },

    setMarginLeft : function setMarginLeft(marginLeft) {
        $('#ts-slideshow-' + this.elId).css(
            {
                'margin-left': marginLeft
            }
        );
        return this;
    },

    initMarginLeft : function initMarginLeft() {
        var numberOfImages = this.images[this.elId].length + 1; // +1 because of the placeholder
        this.marginLeft = (this.direction === 'left')
            ? ((this.width * 0) - 20)
            : ((this.width * -2) - 30);
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
        // Display slideshow
        $('#ts-slideshow-' + this.elId).show();
        // Display images
        $('#' + this.elId + ' .ts-li').show();
        // Display title
        $(this.nextTitle).show();
    },

    selectNextImage : function selectNextImage() {
        if(this.direction === 'left') {
            this.nextImage = $('#ts-slideshow-' + this.elId).children().first();
            this.firstImage = this.nextImage.next();
            this.firstImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            this.secondImage = this.nextImage.next().next();
            this.secondImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            this.thirdImage = this.nextImage.next().next().next();
            this.thirdImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            var currentId = this.nextImage.attr("id");
            this.nextTitle = '#ts-title-' + currentId;
        } else {
            this.firstImage = $('#ts-slideshow-' + this.elId).children().first();
            this.firstImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            this.secondImage = $('#ts-slideshow-' + this.elId).children().first().next();
            this.secondImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            this.nextImage = $('#ts-slideshow-' + this.elId).children().last();
            this.nextImage.css({
                'display' : 'inline',
                'float'   : 'left'
            });
            var currentId = this.nextImage.attr("id");
            this.nextTitle = '#ts-title-' + currentId;
        }
    },

    detachNextImage : function detachNextImage() {
        this.nextImage.remove();
    },

    attachPlaceHolder : function attachPlaceHolder() {
        if(this.direction === 'left') {
            $('#ts-slideshow-' + this.elId).children().last().after(this.placeHolder);
        } else {
            $('#ts-slideshow-' + this.elId).children().first().before(this.placeHolder);
        }
    },

    replacePlaceHolder : function replacePlaceHolder() {
        // This is needed because jQuery removes the elements after replaceWith called
        var tempImageObject = this.nextImage.clone();
        $('#placeholder').replaceWith(tempImageObject);
    },


    clearCursorPosition : function clearCursorPosition() {
        this.lastCursorX = -1;
        this.lastCursorY = -1;
    },

    initIndent : function initIndent() {
        var width = this.width;
        // get the target position for the slideshow div
        if(this.lastDirection === this.direction) {
            this.setMarginLeft(this.marginLeft);
            if(this.direction === 'left') {
                this.indent = this.marginLeft - width;
            } else {
                this.indent = this.marginLeft+width;
            }
        } else {
            this.indent = this.marginLeft;
            this.lastDirection = this.direction;
        }
        this.lastIndent = this.indent;
        return this;
    },

    initAnimateProps : function initAnimateProps() {
        // jQuery.animate() will use this one!
        this.animateProp = {
            marginLeft :this.indent,
            top        :"0px"
            //paddingLeft:"0px"
        };
        return this;
    },

    animate : function animate() {
        var callbackParam = this;
        var titleCallback = this.animateTitle;
        var buttonCallback = this.animateButtons;
        $('#ts-slideshow-'+this.elId).stop().animate(
            this.animateProp,
            {
                easing: 'easeOutCubic',
                duration: this.animationDuraion,
                complete: function() {
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
        return Slider.prototype.actionTransitionFunctions[anotherState][anotherEventType].call(this, event);
    },

    startTicker : function startTicker() {
        var mySelf = this;
        this.currentTicker = setInterval(function() {
            // Slide!
            mySelf.doActionTransition('Inactive','timetick',undefined);
        }, this.animationFrequency);

        return this;
    },

    clearTicker : function clearTicker() {
        if (this.currentTicker) clearInterval(this.currentTicker);
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
        this.cancelTicker();
        alert("TransparentSlider handled unexpected event '" + event.type + "' in state '" + this.currentState + "' for id='" + this.htmlElement.id + "' running browser " + window.navigator.userAgent);
        return this.initialState;
    },

    undefinedState: function undefinedState(event, state) {
        this.cancelTicker();
        alert("TransparentSlider transitioned to undefined state '" + state + "' from state '" + this.currentState + "' due to event '" + event.type + "' from HTML element id='" + this.htmlElement.id + "' running browser " + window.navigator.userAgent);
        return this.initialState;
    },

    actionTransitionFunctions : {

        Inactive: {
            init: function init(event) {
                this.initState();
                this.initImages();
                this.initEventListener();
                this.initSlideshowPanel();
                this.initMarginLeft();
                return this.doActionTransition('Inactive', 'run', event);
            },

            run: function run(event) {
                if(this.lastDirection === this.direction) {
                    this.initMarginLeft();
                    this.selectNextImage();
                }
                this.initPrev();
                this.initNext();
                this.displaySlideshow();
                return this.doActionTransition('Inited', 'move', event);
            },

            timetick: function timetick(event) {
                return this.doActionTransition('Inactive', 'run', event);
            },

            onnext: function onnext(event) {
                this.setDirectionRight();
                this.clearTicker();
                return this.doActionTransition('Inactive', 'run', event);
            },

            onprev: function onprev(event) {
                this.setDirectionLeft();
                this.clearTicker();
                return this.doActionTransition('Inactive', 'run', event);
            },

            click: function click(event) {
                var $eventTarget = $(event.target);
                if($eventTarget.parent().attr('id') === 'next' || $eventTarget.attr('id') === 'next') {
                    return this.doActionTransition('Inactive', 'onnext', event);
                } else {
                    return this.doActionTransition('Inactive', 'onprev', event);
                }
            }

        },

        Inited: {
            move: function move(event) {
                if(this.lastDirection === this.direction) {
                    this.initPlaceHolder();
                    this.attachPlaceHolder();
                    this.replacePlaceHolder();
                }
                return this.doActionTransition('Move', 'slide', event);
            },

            onnext: function onnext(event) {
                return this.doActionTransition('Inactive', 'onnext', event);
            },

            onprev: function onprev(event) {
                return this.doActionTransition('Inactive', 'onprev', event);
            },

            click: function click(event) {
                var $eventTarget = $(event.target);
                if($eventTarget.attr('id') === 'next') {
                    return this.doActionTransition('Inited', 'onnext', event);
                } else {
                    return this.doActionTransition('Inited', 'onPrev', event);
                }
            }

        },

        Move: {
            slide: function slide(event) {
                this.initIndent();
                this.initAnimateProps();
                this.animate();
                return this.doActionTransition('Finish', 'reset', event);
            },

            onnext: function onnext(event) {
                this.setDirectionRight();
                return this.doActionTransition('Interrupt', 'slide', event);
            },

            onprev: function onprev(event) {
                this.setDirectionLeft();
                return this.doActionTransition('Interrupt', 'slide', event);
            },

            click: function click(event) {
                var $eventTarget = $(event.target);
                if($eventTarget.attr('id') === 'next') {
                    return this.doActionTransition('Move', 'onnext', event);
                } else {
                    return this.doActionTransition('Move', 'onPrev', event);
                }
            }

        },

        Interrupt: {
            slide: function slide() {
                return this.doActionTransition('Move', 'slide', event);
            }
        },

        Finish: {
            reset: function reset(event) {
                this.hideTitle();
                this.detachNextImage();
                this.clearCursorPosition();
                this.clearTicker();
                this.startTicker();
                return "Inactive";
            }
        }

    }

};
