class Modal {
    constructor(id) {
        this.id = id;
        this.isVisible = "is-visible";
    }

    open() {
        document.getElementById(this.id).classList.add(this.isVisible);
    }

    close() {
        document.getElementById(this.id).classList.remove(this.isVisible);
    }
}

class ConfirmDialog {
    constructor(message) {
        this.message = message;
        this.isVisible = "is-visible";
        document.getElementById("confirm_message").innerHTML = this.message;
        document.getElementById("yes_btn").addEventListener("click", function (){
           alert("Yes");
        });

    }

    open() {
        document.getElementById("dialog_confirm").classList.add(this.isVisible);
    }

    close() {
        document.getElementById("dialog_confirm").classList.remove(this.isVisible);
    }

    confirm() {
        alert("Hello");
    }

}



const isVisible = "is-visible";

function showLoading() {
    document.getElementById("dialog_loading").classList.add(isVisible);
}

function hideLoading() {
    document.getElementById("dialog_loading").classList.remove(isVisible);
}

function openSuccessDialog(message) {
    document.getElementById("success_message").innerHTML = message;
    document.getElementById("dialog_success").classList.add(isVisible);
}

function closeSuccessDialog() {
    document.getElementById("dialog_success").classList.remove(isVisible);
}

function openErrorDialog(message) {
    document.getElementById("error_message").innerHTML = message;
    document.getElementById("dialog_error").classList.add(isVisible);
}

function closeErrorDialog() {
    document.getElementById("dialog_error").classList.remove(isVisible);
}

function showConfirmDialog(message, confim) {
    document.getElementById("confirm_message").innerHTML = message;
    document.getElementById("dialog_confirm").classList.add(isVisible);
    const event = document.getElementById("yes_btn");
    const fun = function (){
        confim();
        event.removeEventListener("click", fun);
    }
    event.addEventListener("click", fun);

}

const closeEls = document.querySelectorAll("[data-close]");
for (const el of closeEls) {
    el.addEventListener("click", function() {
        this.parentElement.parentElement.parentElement.classList.remove(isVisible);
    });
}