# Kiro Settings Configuration

## MCP Configuration

To set up the Model Context Protocol (MCP) servers, you need to create a `mcp.json` file in this directory.

### Setup Instructions

1. Copy the template file:
   ```bash
   cp mcp.json.template mcp.json
   ```

2. Edit `mcp.json` and replace the placeholder values:
   - `YOUR_BRAVE_API_KEY_HERE`: Get your API key from [Brave Search API](https://api.search.brave.com/)
   - `YOUR_GITHUB_TOKEN_HERE`: Create a personal access token from [GitHub Settings](https://github.com/settings/tokens)

### Security Note

**IMPORTANT**: The `mcp.json` file contains sensitive API keys and tokens. It is automatically ignored by git to prevent accidental commits. Never commit this file to version control.

### Required Permissions

For the GitHub token, ensure it has the following permissions:
- `repo` (for repository access)
- `read:user` (for user information)
- `read:org` (if working with organization repositories)

### Troubleshooting

If MCP servers are not working:
1. Verify your API keys are correct
2. Check that the required npm packages are available
3. Ensure your network allows the required connections
4. Check the Kiro logs for specific error messages