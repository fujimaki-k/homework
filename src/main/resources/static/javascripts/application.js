window.addEventListener("load", () => {
    document.querySelectorAll("a.remove").forEach((element) => {
        element.addEventListener("click", (event) => {
            const result = window.confirm(`${event.target.title.replace("する", "しますか？")}`);
            if (result) {
                return true;
            }

            event.preventDefault();
            return false;
        });
    });
});
