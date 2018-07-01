window.addEventListener("load", () => {
    document.querySelectorAll("a.remove").forEach((element) => {
        element.addEventListener("click", (event) => {
            const result = window.confirm("本当に削除しますか？");
            if (result) {
                return true;
            }

            event.preventDefault();
            return false;
        });
    });
});
