const btnId = "wkndBtn";
const wkndId = "wkndItems";

document.addEventListener("click", (e) => {
    const btn = document.getElementById(btnId);
    const wkndItems = document.getElementById(wkndId);

    if (e.target === btn) {
        wkndItems.innerText = "Hurray, you clicked the button!";
    } else {
        wkndItems.innerText = "Awww, you did not click the button!";
    }
});