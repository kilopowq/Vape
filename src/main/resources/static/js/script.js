class LoginApp {
    run() {
        AppConfig.load().then(() => {
            this.constructor.captureToken();
            this.redirectIfTokenExpired();
            this.renderContent();
            this.setupHandlers();
        });
    }

    static getToken() {
        return localStorage.getItem('id_token');
    }

    setupHandlers() {
        new Clipboard('#copy-to-clipboard');
        $('#refresh-token').click(this.login.bind(this));
    }

    login() {
        const conf = AppConfig;
        location.href = `${conf.authorizeUrl}?client_id=${conf.clientId}&redirect_uri=${conf.redirectUri}&response_type=id_token%20token&nonce=${this.constructor.nonce()}&scope=openid`;
    }

    renderContent() {
        const token = this.constructor.getToken();
        if(token !== null) {
            $('#token').text(`Bearer ${token}`);
            const decoded = jwt_decode(token);
            $('#token-expires').text(moment.unix(decoded.exp).fromNow());
            $('#token-contents').text(JSON.stringify(decoded, null, 2))
        }
    }

    static captureToken() {
        const rawHash = window.location.hash.substring(1);
        const authParams = _.fromPairs(rawHash.split('&').map(i => i.split('=')));
        if(authParams.id_token) {
            localStorage.setItem('id_token', authParams.id_token);
            history.replaceState({}, document.title, window.location.pathname + window.location.search);
        }
    }

    redirectIfTokenExpired() {
        if(!this.constructor.isTokenValid())
            this.login();
    }

    static isTokenValid() {
        const token = this.getToken();
        if(token === null)
            return false;

        const timestamp = new Date().getTime() / 1000;
        const expiryTimestamp = jwt_decode(token).exp;

        if(timestamp > expiryTimestamp)
            return false;
        return true;
    }

    static nonce(length = 8) {
        let text = "";
        const possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for(let i = 0; i < length; i++) {
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        }
        return text;
    }
}
