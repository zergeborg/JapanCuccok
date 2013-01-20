jQuery.fx.interval = 30;
(function($){

    Function.prototype.bind = function(scope) {
        var _function = this;

        return function() {
            return _function.apply(scope, arguments);
        }
    }

    var defaults = {
        duration: 1500,
        easing: ''
    };

    $.fn.redraw = function(){
        $(this).each(function(){
            var redraw = this.offsetHeight;
        });
    };

    $.fn.emulateTransitionEnd = function(duration) {
        var called = false, $el = this;
        $(this).one('webkitTransitionEnd', function() { called = true; });
        $(this).one('transitionEnd', function() { called = true; });
        var callback = function() { if (!called) $($el).trigger('webkitTransitionEnd'); };
        setTimeout(callback, duration);
    };

    $.fn.transition = function (properties, options, callback) {
        options = $.extend({}, defaults, options);
        properties['webkitTransition'] = 'all ' + options.duration + 'ms ' + options.easing;
        properties['transition'] = 'all ' + options.duration + 'ms ' + options.easing;
        $(this).one('webkitTransitionEnd', callback);
        $(this).one('transitionEnd', callback);
        $(this).emulateTransitionEnd(options.duration + 50);
        $(this).css(properties);
    };

    $.fn.extend({
        transparentSlider: function(options) {

            var mySelf = this;

            var defaults =
            {
                elId                 : undefined,
                ulObject             : undefined,
                params               : [],
                lastTitle            : undefined,
                nextTitle            : undefined,
                direction            : 'left', // Which direction?
                lastDirection        : 'left', // The direction in the previous round
                lastCursorX          : 0, // cursor x-position at most recent mouse event
                lastCursorY          : 0, // cursor y-position at most recent mouse event
                sliderWidth          : '60em',
                sliderHeight         : '30em',
                imageWidth           : '60em',
                imageHeight          : '30em',
                thumbnailWidth       : '200px',
                thumbnailHeight      : '150px',
                titleOpacity         : 1, // opacity of title and navigation
                titleSpeed           : 1000, // speed of title display
                titleHeight          : 90, // height of the titles
                animationFrequency   : 6000,
                animationDuraion     : 1500,
                left                 : 0,
                lastLeft             : 0,
                initialState         : 'Inactive',
                currentState         : 'Inactive',
                animateProp          : undefined,
                lastImage            : undefined,
                nextImage            : undefined,
                currentTicker        : undefined // returned by setInterval, if a ticker is currently running
            };
            var options = $.extend(defaults, options);

            return this.each(function() {

                var mySelf = this;
                var randID = Math.round(Math.random()*100000000);
                var numImages;
                var curr = 1;
                var dist = 0;
                var o=options;
                // createMovingUl()
                var movingImages;
                var movingUl;
                o.elId = $(mySelf).attr('id');
                o.params[o.elId] = $.extend({}, o);
                o.sliderWidth = o.params[o.elId].sliderWidth;
                o.sliderHeight = o.params[o.elId].sliderHeight;

                setDirectionRight : function setDirectionRight() {
                    o.lastDirection = o.direction;
                    o.direction = 'right';
                }

                setDirectionLeft : function setDirectionLeft() {
                    o.lastDirection = o.direction;
                    o.direction = 'left';
                }

                createTitleBar: function createTitleBar(i) {
                    // create title bar
                    $('#labelWrapper').append("<div class='ts-title' id='ts-title-" + o.elId + i +"'></div>");
                    var $title = $('#ts-title-' + o.elId + i);
                    return $title;
                }

                initTitleBar: function initTitleBar($title, $titleContent) {
                    //$title.css({'height' : o.params[o.elId].titleHeight});
                    $title.html($titleContent);
                    return o;
                }

                initImages : function initImages() {
                    var imgList = $('.imageWrapper div', '#slider');
                    for(var i = 0, max = imgList.length; i < max; i += 1) {
                        var $placeHolderDiv = $(imgList[i]);
                        var $spanWithBgImage = $placeHolderDiv.parent();
                        var $spanContainingTitle = $spanWithBgImage.next();
                        var $title = createTitleBar(i);
                        var $titleContent = $spanContainingTitle.is('span') ? $spanContainingTitle.html() : '';
                        $spanContainingTitle.hide();
                        initTitleBar($title, $titleContent);
                        var $ts_li_child = $spanWithBgImage.parent();
                        var $ts_li = $ts_li_child.parent();
                        $placeHolderDiv.css( { 'width': o.imageWidth } );
                        $placeHolderDiv.css( { 'height': o.imageHeight } );
                        $placeHolderDiv.redraw();
                        $ts_li.css( { 'width': o.imageWidth } ).hide();
                    }
                    return o;
                }

                //Event listener means the element which is used as the target to attach the content to
                initEventListener : function initEventListener() {
                    var sliderWidth = o.sliderWidth;
                    var sliderHeight = o.sliderHeight;
                    // set panel
                    $('#' + o.elId).css({
                        'width'   :sliderWidth,
                        'height'  :sliderHeight
                    }).wrap("<div class='transparent-slider' id='transparent-slider-" + o.elId + "' />");

                    o.imageWidth = $('#' + o.elId).css('width');
                    o.imageHeight = $('#' + o.elId).css('height');

                    // add slideshow to the DOM tree
                    $('.ts-slideshow', '#' + o.elId).attr('id', 'ts-slideshow-' + o.elId);
                    o.ulObject = $('#ts-slideshow-' + o.elId);
                    numImages = $('li', o.ulObject).length;
                    return o;
                }

                initSlideshowPanel : function initSlideshowPanel() {
                    var slideshowWidth = o.sliderWidth;
                    var slideshowHeight = o.sliderHeight;
                    // slideshow width should be the width of [image_width]*[nr of images]
                    o.ulObject.css(
                        {
                            'width'  :300 + (10 * 3) + '%', // 10 equals to the left and right margins between images
                            'height' :slideshowHeight
                        }
                    );
                    return o;
                }

                initThumbnails : function initThumbnails()
                {
                    var object = $("#sliderAndLabel");
                    var numImages = $('li', object).length;
                    var imageWidth = o.sliderWidth;
                    var thumb,i;
                    // Build thumbnail viewer and thumbnail divs
                    object.after('<div class="thumbs" id="thumbs'+randID+'"></div>');
                    var $thumbs = $('#thumbs'+randID);
                    $thumbs.css( { 'width': imageWidth } );
                    for(i=0;i<numImages;i++)
                    {
                        thumb = $('.ts-li .imageWrapper:eq('+(i)+')', object).css('background-image');
                        $thumbs.append('<div class="thumb" id="thumb'+randID+'_'+(i)+'" style="background-size:'+o.thumbnailWidth+';width:'+o.thumbnailWidth+';height:'+o.thumbnailHeight+';line-height:'+o.thumbnailHeight+';"></div>');
                        var $thumb_i = $('#thumb'+randID+'_'+ i);
                        $thumb_i.css({'background-image':thumb});
                        if(i===0) $('#thumb'+randID+'_1').css({'border-color':'#ff0000'});
                    }
                    // Next two lines are a special case to handle the first list element which was originally the last
                    thumb = $('.ts-li .imageWrapper:first', object).css('background-image');
                    $('#thumb'+randID+'_'+numImages).css({'background-image':thumb});

                    // Assign click handler for the thumbnails. Normally the format $('.thumb') would work but since it's outside of our object (obj) it would get called multiple times
                    $('#thumbs'+randID+' div').bind('click', handleEvent); // We use bind instead of just plain click so that we can repeatedly remove and reattach the handler
                }

                thumbClick : function thumbClick(event)
                {
                    var $eventTarget = $(event.target);
                    var id = $eventTarget.attr('id');
                    var target_num = id.split('_')[1];
                    hideTitle();
                    detachNextImage();
                    clearCursorPosition();
                    clearTicker();
                    startTicker();
                    if(curr != target_num)
                    {
                        $("[id*='thumb"+randID+"']").css({'border-color':'rgba(0, 0, 0, 0.3)'});
                        $('#thumb'+randID+'_'+target_num).css({'border-color':'#ccc'});
                    }
                    if(target_num > curr)
                    {
                        dist = target_num - curr;
                        setDirectionLeft();
                    }
                    if(target_num < curr)
                    {
                        dist = curr - target_num;
                        setDirectionRight();
                    }
                    clearTicker();
                }

                setLeft : function setLeft(leftPosition) {
                    o.ulObject.css(
                        {
                            left: leftPosition
                        }
                    );
                    return o;
                }

                displaySlideshow : function displaySlideshow() {
                    // Display slideshow
                    o.ulObject.show();
                    // Display images
                    // $('#' + o.elId + ' .ts-li').show();
                    // Display title
                    $(o.nextTitle).show();
                }

                cloneNextImage : function cloneNextImage() {
                    if(o.direction === 'left') {
                        $('li:last', o.ulObject).after(o.nextImage.clone().css({'position': 'absolute'}).hide());
                    } else {
                        $('li:first', o.ulObject).before(o.nextImage.clone().css({'position': 'absolute'}));
                    }
                }

                detachNextImage : function detachNextImage() {
                    o.nextImage.remove();
                }

                clearCursorPosition : function clearCursorPosition() {
                    o.lastCursorX = -1;
                    o.lastCursorY = -1;
                }

                animateTitle : function animateTitle(o) {
                    $(o.nextTitle).css({ 'position':'absolute' }).show().animate(
                        {},
                        {
                            duration: o.titleSpeed,
                            complete: function() {
                                $(o.nextTitle).css({ 'position':'relative' });
                            }
                        }
                    );
                }

                animateButtons : function animateButtons(o) {
                    $('#next').css({ 'position':'absolute', 'opacity':0 }).animate(
                        {
                            'opacity':o.titleOpacity
                        },
                        {
                            duration: o.titleSpeed,
                            complete: function() {
                                $('#next').css({ 'opacity':1 });
                            }
                        }
                    );
                    $('#prev').css({ 'position':'absolute', 'opacity':0 }).animate(
                        {
                            'opacity':o.titleOpacity
                        },
                        {
                            duration: o.titleSpeed,
                            complete: function() {
                                $('#prev').css({ 'opacity':1 });
                            }
                        }
                    );
                }

                hideTitle : function hideTitle() {
                    $('.ts-title', '#titleContainer').hide();
                }

                doActionTransition : function doActionTransition(anotherState, anotherEventType, event) {
                    return actionTransitionFunctions[anotherState][anotherEventType].call($(mySelf), event);
                }

                startTicker : function startTicker() {
                    o.currentTicker = setInterval(function() {
                        // Slide!
                        doActionTransition('Inactive','timetick',undefined);
                    }, o.animationFrequency);

                    return o;
                }

                clearTicker : function clearTicker() {
                    var ticker = o.currentTicker;
                    if (ticker) clearInterval(ticker);
                    o.currentTicker = null;
                    return o;
                }

                multiStep : function multiStep(steps, args, callback) {

                    var tasks = steps.concat(); //clone the array

                    setTimeout(function(){

                        //execute the next task
                        var task = tasks.shift();
                        task.apply(null, args || []);

                        //determine if there is more
                        if(tasks.length > 0) {
                            setTimeout(arguments.callee, 25);
                        } else {
                            callback();
                        }
                    }, 25);
                }

                handleEvent: function handleEvent(event) {
                    var actionTransitionFunction = actionTransitionFunctions[o.currentState][event.type];
                    if (!actionTransitionFunction) actionTransitionFunction = unexpectedEvent;
                    var nextState = actionTransitionFunction.call(mySelf, event);
                    if (!nextState) nextState = o.currentState;
                    if (!actionTransitionFunctions[nextState]) nextState = undefinedState(event, nextState);
                    o.currentState = nextState;
                }

                unexpectedEvent: function unexpectedEvent(event) {
                    clearTicker();
                    alert("TransparentSlider handled unexpected event '" + event.type + "' in state '" + o.currentState + "' for id='" + o.elId + "' running browser " + window.navigator.userAgent);
                    return o.initialState;
                }

                undefinedState: function undefinedState(event, state) {
                    clearTicker();
                    alert("TransparentSlider transitioned to undefined state '" + state + "' from state '" + o.currentState + "' due to event '" + event.type + "' from HTML element id='" + o.elId + "' running browser " + window.navigator.userAgent);
                    return o.initialState;
                }

                var actionTransitionFunctions = {

                    Inactive: {
                        init: function init(event) {
                            initEventListener();
                            initSlideshowPanel();
                            initImages();
                            initThumbnails();
                            displaySlideshow();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        run: function run(event) {
                            if(o.lastDirection === o.direction) {
                                if(movingImages !== undefined && movingImages.parent().attr('id') !== o.ulObject.attr('id')) {
                                    movingImages.hide();
                                    movingImages.unwrap();
                                    movingImages.css({ 'position': 'absolute'});
                                }
                                // selectNextTitle()
                                var $li_first = $('li:first', o.ulObject);
                                var $li_last = $('li:last', o.ulObject);
                                // selectNextImage()
                                if(o.direction === 'left') {
                                    o.nextImage =  $li_first;
                                    o.lastImage = o.nextImage;
                                    o.lastTitle = o.nextTitle;
                                    o.nextTitle = '#ts-title-' + $('li:first + li + li', o.ulObject).attr("id");
                                } else {
                                    o.nextImage = $li_last;
                                    o.lastImage = o.nextImage;
                                    o.lastTitle = o.nextTitle;
                                    o.nextTitle = '#ts-title-' + $li_last.attr("id");
                                }
                                cloneNextImage();
                                if(o.direction === 'left') {
                                    movingImages = $('li:first, li:first + li, li:first + li + li', o.ulObject);
                                } else {
                                    movingImages = $('li:first, li:first + li, li:last', o.ulObject);
                                }
                                movingImages.css({ 'position': 'static' });
                                movingImages = movingImages.show().wrapAll("<ul />");
                                movingUl = movingImages.parent();
                                movingUl.attr('class', 'movingUl');
                                movingUl.css({ 'position':'absolute' });
                            } else {
                                o.nextTitle = o.lastTitle;
                                o.nextImage = o.lastImage;
                            }
                            // initPrev()
                            $('#prev').css({'opacity':'0', 'z-index':'9999'});
                            // initNext()
                            $('#next').css({'opacity':'0', 'z-index':'9999'});
                            // initCurrent()
                            var object = o.ulObject;
                            if(o.direction === 'left') {
                                if(curr === numImages-1) {
                                    curr = 0;
                                } else {
                                    curr = curr+1;
                                }
                            } else {
                                if(curr === 0) {
                                    curr = numImages-1;
                                } else {
                                    curr = curr-1;
                                }
                            }
                            // moveThumbnailBorder()
                            $("[id*='thumb"+randID+"']").css({'border-color':'rgba(0, 0, 0, 0.3)'});
                            $('#thumb'+randID+'_'+curr).css({'border-color':'#ccc'});
                            // initLeft()
                            var width = o.imageWidth;
                            var direction = o.direction;
                            // animate()
                            var titleCB = animateTitle;
                            var buttonCB = animateButtons;
                            if ('WebkitTransform' in document.body.style
                                || 'MozTransform' in document.body.style
                                || 'OTransform' in document.body.style
                                || 'transform' in document.body.style)
                            {
                                // get the target position for the slideshow div
                                if(direction === 'left') {
                                    var translateValue = '-50px';
                                    movingUl.css({ 'transform' : 'translate('+ translateValue +', 0)' });
                                    o.left = (- parseInt(width) - 50 - 20) + 'px';
                                } else {
                                    var translateValue = (- parseInt(width) - 50 - 20) + 'px';
                                    movingUl.css({ 'transform' : 'translate('+ translateValue +', 0)' });
                                    o.left = '-50px';
                                }
                                o.lastDirection = direction;
                                o.lastLeft = o.left;
                                movingUl.redraw();
                                movingUl.transition(
                                    { 'transform' : 'translate('+o.left+', 0)' },
                                    {},
                                    function() {
                                        var callbackParam = o;
                                        var titleCallback = titleCB;
                                        var buttonCallback = buttonCB;
                                        titleCallback(callbackParam);
                                        buttonCallback(callbackParam);
                                    }
                                );
                            } else {
                                // get the target position for the slideshow div
                                if(o.lastDirection === direction) {
                                    if(direction === 'left') {
                                        movingUl.css(
                                            {
                                                'left': - 30
                                            }
                                        );
                                        o.left = - width - 50;
                                    } else {
                                        movingUl.css(
                                            {
                                                'left': (width)-(numImages*10)-40
                                            }
                                        );
                                        o.left = - 30;
                                    }
                                } else {
                                    if(direction === 'left') {
                                        o.left = o.lastLeft - width - 20;
                                    } else {
                                        o.left = o.lastLeft + width + 20;
                                    }
                                    o.lastDirection = direction;
                                }
                                movingUl.redraw();
                                o.lastLeft = o.left;
                                // initAnimateProps()
                                movingUl.stop().animate(
                                    { 'left' : o.left },
                                    {
                                        easing: 'easeOutBack',
                                        duration: o.animationDuraion,
                                        complete: function() {
                                            var callbackParam = o;
                                            var titleCallback = titleCB;
                                            var buttonCallback = buttonCB;
                                            titleCallback(callbackParam);
                                            buttonCallback(callbackParam);
                                        }
                                    }
                                );
                            }
                            hideTitle();
                            detachNextImage();
                            clearCursorPosition();
                            clearTicker();
                            startTicker();
                            return "Inactive";
                        },

                        timetick: function timetick(event) {
                            return doActionTransition('Inactive', 'run', event);
                        },

                        onnext: function onnext(event) {
                            hideTitle();
                            detachNextImage();
                            clearCursorPosition();
                            clearTicker();
                            startTicker();
                            setDirectionLeft();
                            clearTicker();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        onprev: function onprev(event) {
                            hideTitle();
                            detachNextImage();
                            clearCursorPosition();
                            clearTicker();
                            startTicker();
                            setDirectionRight();
                            clearTicker();
                            return doActionTransition('Inactive', 'run', event);
                        },

                        onthumb: function onthumb(event) {
                            var j;
                            thumbClick(event);
                            for(j = 0; j < dist; j++)
                            {
                                doActionTransition('Inactive', 'run', event);
                            }
                            dist = 0;
                            return "Inactive";
                        },

                        click: function click(event) {
                            var $eventTarget = $(event.target);
                            if($eventTarget.parent().attr('id') === 'next' || $eventTarget.attr('id') === 'next') {
                                return doActionTransition('Inactive', 'onnext', event);
                            } else if($eventTarget.parent().attr('class') === 'thumb' || $eventTarget.attr('class') === 'thumb') {
                                return doActionTransition('Inactive', 'onthumb', event);
                            } else {
                                return doActionTransition('Inactive', 'onprev', event);
                            }
                        }

                    }

                };

                (function () {
                    $('#next').click(function (event) {
                        handleEvent(event);
                    });
                })();

                (function () {
                    $('#prev').click(function (event) {
                        handleEvent(event);
                    });
                })();

                doActionTransition('Inactive','init',undefined);
            })
        }
    });
})(jQuery);