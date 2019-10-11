const AppConfig = {
    load: () => new Promise((resolve, reject) =>
        $.get('login/config', data => {
            _.extend(AppConfig, data);
            AppConfig.redirectUri = `${window.location.origin}/login`;
            resolve();
        })
    )
};

