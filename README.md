# Digital Writing
<p align="center"><img width="200px" src="/images/logo.png" alt="Digital write"/></p>

## Цільова група
Будь-який користувач, 
який хоче пройти аутентифікацію.

---

## Мета
>Прискорення ідентифікації користувача на основі 
>розпізнавання цифрового почерку через створення 
>Rest API сервісу.

---

## Cписок учасників команди
- Таужнянський А.В., АІ-192
- Макаренко А.О., АІ-192

---

## Приклади функцій для визначення параметрів почерку користувача

Функції написані на `JavaScript`:
- визначення швидкості набору тексту:
   
 ```javascript
const typingSpeed = () => {
	let date= new Date();
	let endTime= date.getTime();
	let timeTaken= (endTime-startTime-800)/1000;
	console.log(timeTaken);

	let totalWords= (message.innerText.split(" ")).length;
	if((text.value.trim()).length>0)
	{
		var wordsCount= (text.value.split(" ")).length;
	}
	else {
		var wordsCount=0;
	}

	# Швидкість 
	let speed= Math.round(((60/timeTaken)*wordsCount));
	
   }
  ```
- визначення точності набору тексту:
 ```javascript
const accuracy = (str, message) => {
    message = (message.split(" "));
    let count = 0;
    console.log(message);
    str.trim().split(" ").forEach(function (item) {
        if (message.indexOf(item) > -1)
            count++;

    });
    
    #Точність
    return count;
}
 ```
