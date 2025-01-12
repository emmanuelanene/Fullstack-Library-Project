export const oktaConfig = {
    clientId: '0oaju4zsbxcxXmPOY5d7',
    issuer: 'https://dev-12591642.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    audience: 'api://default',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true,
}