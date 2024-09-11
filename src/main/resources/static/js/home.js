function changeTextButton(){
    var changeBtn = document.getElementById("changeButton");
    if(changeBtn.value == "click me!"){
        changeBtn.value = "Clicked!";
        changeBtn.classList.add("green");
        changeBtn.classList.remove("red");
    } else{
        changeBtn.value = "click me!";
        changeBtn.classList.add("red");
        changeBtn.classList.remove("green");
    }
}