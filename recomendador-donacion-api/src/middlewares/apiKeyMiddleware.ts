import { error } from "console";
import { NextFunction, Request, Response } from "express";
import { validarApiKey } from "../services/apiKeyService";

export async function apiKeyMiddleware(
	req: Request,
	res: Response,
	next: NextFunction
) {
	const key: string | undefined = req.headers.authorization;

	if (!key) {
		res.status(403).json({ error: "No credentials were sent!" });
	}

	if (await validarApiKey(key!)) {
		next();
	} else {
		res.status(403).json({ error: "Invalid API Key!" });
	}
}