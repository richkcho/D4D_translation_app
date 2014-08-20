$( document ).ready(function() {
$('.level_1').on('click', spawn);
});

function spawn() {
// we want to get the current class and the number of children. We use the level to get the number of children
var className = this.className;
console.log(className);
var level = (className.length - 5) / 2;
var newLevel = level + 1;
console.log(newLevel);
var currentCount = $(this).parent().children(".divLevel_" + newLevel).length + 1;
console.log(currentCount);

var x = addField(newLevel, className, currentCount);

$(this).parent().append(x);
}

var addField = function(newLevel, className, currentCount) {
var newDiv = $('<div class="divLevel_' + newLevel + '"></div>');
var inputA = $('<input autocomplete="off" id="' + className + '_' + currentCount + '_a" name="' + className + '_' + currentCount + '_a" type="text" placeholder="Leaf: ' + className + '_' + currentCount + '_a"/>');
var inputB = $('<input autocomplete="off" id="' + className + '_' + currentCount + '_b" name="' + className + '_' + currentCount + '_b" type="text" placeholder="Leaf: ' + className + '_' + currentCount + '_b"/>');
var button = $('<button class="' + className + '_' + currentCount + '" type="button">+Leaf</button>');
button.on('click', spawn);

newDiv.append(inputA).append(inputB).append(button);

newDiv.css('margin-left', '20px');
newDiv.css('padding-left', '10px');
newDiv.css('border-left', 'solid 1px red');

return newDiv;
}
