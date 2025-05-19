import axios from 'axios';

let accountId = null;
const BASE_URL = '/api/account';
const USER_ID = 20;

const initializeUser = async () =>{
    if(accountId) return accountId;
    try{
        const response = await axios.get(`${BASE_URL}/user/${USER_ID}`);
        accountId = response.data.id;
        return accountId;
    }
    catch(error){
        console.error(error);
        throw error;
    }
};

const getAccountId = async () => {
    if(!accountId){
        return await initializeUser();
    }
    return accountId;
};

const  getBalance = async () => {
    const accountId = await getAccountId();
    try {
        const response = await axios.get(`${BASE_URL}/${accountId}/balance`);
        return response.data;
    }
    catch(error){
        console.error('Failed to get balance:', error);
        throw error;
    }
};

const getAssets = async () =>{
    const accountId = await getAccountId();
    try {
        const response = await axios.get(`${BASE_URL}/${accountId}/assets`);
        return response.data;
    }
    catch(error){
        console.error('Failed to get assets:', error);
        throw error;
    }
};

const getTransactions = async () =>{
    const accountId = await getAccountId();
    try {
        const response = await axios.get(`${BASE_URL}/${accountId}/transactions`);
        return response.data;
    }
    catch(error){
        console.error('Failed to get transactions:', error);
        throw error;
    }
};

const buyCrypto = async (symbol, quantity) => {
    const accountId = await getAccountId();
    try {
        const response = await axios.post(`${BASE_URL}/${accountId}/buy`, {symbol, quantity:(Number(quantity))});
        return response.data;
    }
    catch(error){
        console.error('Failed to buy crypto:', error);
        throw error;
    }
};

const sellCrypto = async (symbol, quantity) => {
    const accountId = await getAccountId();
    try {
        const response = await axios.post(`${BASE_URL}/${accountId}/sell`, {symbol, quantity:(Number(quantity))});
        return response.data;
    }
    catch(error){
        console.error('Failed to sell crypto:', error);
        throw error;
    }
};

const resetAccount = async () => {
    const accountId = await getAccountId();
    try {
        const response = await axios.post(`${BASE_URL}/${accountId}/reset`);
        return response.data;
    }
    catch(error){
        console.error('Failed to reset account:', error);
        throw error;
    }
};

const isLoggedIn = () => {
    return accountId !== null;
};

const accountService = {
    initializeUser,
    getBalance,
    getAssets,
    getTransactions,
    buyCrypto,
    sellCrypto,
    resetAccount,
};
export default accountService;



