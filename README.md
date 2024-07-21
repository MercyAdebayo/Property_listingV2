# Property Listing Web Application Using Angular and .NET Core Web API

## Executive Summary
This project aims to develop a comprehensive web application that enables property sellers to list properties for sale effectively. Utilizing Angular for the frontend and .NET Core Web API for the backend, this application will provide a robust, user-friendly platform for real estate transactions. The project addresses the need for a modern, efficient, and accessible online property listing service.

## Restoring the WEBAPI Project

### Steps to Restore the WEBAPI Project
1. **Clone the WEBAPI Project:**
    ```bash
    git clone <repository-url>
    ```
2. **Open the Solution File:**
   Open the solution file in your preferred IDE (e.g., Visual Studio).

3. **Change Your Connection String:**
   Update the connection string in the `appsettings.json` file to point to your database.

4. **Update the Database:**
    ```bash
    dotnet ef database update
    ```
    This command will apply any pending migrations to the database.

5. **Run the Application:**
   Once the database is updated successfully, run the application.

6. **View the Swagger Documentation:**
   Navigate to `/index.html` in your browser to view the Swagger documentation for the API.

By following these steps, you will be able to restore and run the WEBAPI project locally.

## House Listing Git Collaboration Policy

### Branch Naming Convention
Each developer must create a branch that ends with their individual name. This branch will be used to manage their contributions and should follow the format: `feature/<feature-name>-<team-name>`.

### Creating Your Feature Branch
1. Ensure you're on the `development` branch to start from the latest version.
    ```bash
    git checkout development
    ```
2. Pull the latest changes from the remote `development` branch.
    ```bash
    git pull origin development
    ```
3. Create a new branch following the naming convention.
    ```bash
    git checkout -b feature/<feature-name>-<team-name>
    ```

### Making Changes and Committing
1. After making changes in your branch, stage your changes.
    ```bash
    git add .
    ```
2. Commit your changes with a descriptive message.
    ```bash
    git commit -m "A concise, descriptive commit message"
    ```

### Pushing Changes to Remote
Before creating a pull request, you need to make sure your local branch is up to date with the latest changes from the `development` branch, should there be any changes to it. After updating, push your changes to the remote repository.

1. Switch to the `development` branch.
    ```bash
    git checkout development
    ```
2. Pull the latest changes from the remote `development` branch.
    ```bash
    git pull origin development
    ```
3. Switch back to your feature branch.
    ```bash
    git checkout feature/<feature-name>-<team-name>
    ```
4. Merge the updates from `development`.
    ```bash
    git merge development
    ```
    Resolve any merge conflicts if they arise. This may involve manually editing files to choose which changes to keep, then staging and committing those changes.
    - After resolving conflicts (if any), commit the changes.

5. Push your branch for the first time.
    ```bash
    git push -u origin feature/<feature-name>-<team-name>
    ```
6. For subsequent pushes, you can simply use:
    ```bash
    git push
    ```

### Creating a Pull Request
1. Go to the repository page on your GitHub.
2. Navigate to the "Pull Requests" section and click on "New pull request".
3. Select `development` as the base branch and your `feature/<feature-name>-<team-name>` branch as the compare branch.
4. Fill in a descriptive title for your pull request that clearly states what it does or the feature it adds.
5. In the description box, provide a detailed explanation of the changes. Include any relevant issue numbers by mentioning them with a hashtag (e.g., `#123`).
6. Before submitting, assign a reviewer.

### Review and Merge
- The assigned reviewer will review the pull request, suggest changes if necessary, and approve it.
- Once approved, the pull request can be merged into the `development` branch. Typically, the person who created the pull request merges it after approval.
- It's good practice to delete the feature branch from the remote repository after merging to keep the branch list tidy.
    ```bash
    git push origin --delete feature/<feature-name>-<team-name>
    ```

### Additional Policies
- **Code Reviews:** All pull requests must be reviewed by at least one other team member before merging. This ensures code quality and maintainability.
- **Conflict Resolution:** If your pull request has conflicts with the `development` branch, you must resolve these conflicts locally on your branch and push the resolved changes before merging.
